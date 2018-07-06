package it.polimi.se2018.controller;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * This controller class handles all the operations that happen during a round. It's not included in a complete Observer
 * Pattern but it handles the update at the same way of main GameController class trough Update method.
 * This class implements the Timer Interface since it has to handle the turn timer.
 */
public class RoundHandler implements TimerInterface {

    public final Game gameAssociated;
    private GameController gameController;
    private int turnNumber = 0;
    private int actualRound;
    protected int movableDice = 1;
    protected long timerID;
    private long moveTimer;
    private Random rand = new Random();

    private final List<Player> turnList;
    private Player activePlayer;
    private WindowPatternCard workingPatternCard;

    /**
     * Round handler constructor
     * @param game: associated game
     */
    public RoundHandler (Game game){
        this.gameAssociated = game;
        this.actualRound = this.gameAssociated.getActualRound();
        this.gameController = gameAssociated.getAssociatedGameController();

        this.moveTimer = Server.getInstance().getDefaultMoveTimer();
        this.turnList = generateTurnList();

        if (this.actualRound - 1 == Server.getInstance().getnOfTurn()){
            this.gameController.onGameEnd();
            return;
        }

        if (this.actualRound != 1 ) {
            this.gameAssociated.getRoundTrack().addDice(this.gameAssociated.getDiceOnTable(), this.actualRound-2);
            List<Die> aD = new ArrayList<>();
            this.gameAssociated.setDiceOnTable(aD); //set empty arraylist for table
        }

        this.extractDice();
        this.gameAssociated.setActivePlayer(turnList.get(turnNumber)); //set the first player as active player
        this.activePlayer = this.gameAssociated.getActivePlayer();
        this.workingPatternCard = this.activePlayer.getActivePatternCard();
        this.timerID = TimerHandler.registerTimer(this, moveTimer); //register new turn timer
        TimerHandler.startTimer(this.timerID);
    }

    /**
     * This method extracts dice from the dice bag and puts them on the table.
     * It's called in the constructor and only once for round
     */
    private void extractDice(){
        int nOfPlayers = this.gameAssociated.getPlayersOrder().size();
        int diceToExtract = nOfPlayers * 2 + 1; //See Sagrada's rules.
        List<Die> dB = this.gameAssociated.getDiceBag();
        List<Die> dT = this.gameAssociated.getDiceOnTable();

        for (int i = 0; i < diceToExtract; i++){
            Die d = dB.get(rand.nextInt(dB.size()));
            if (d.equals(this.gameAssociated.getDieForSwitch()) && this.actualRound != 10){
                i--;
                continue;
            }
            dB.remove(d);
            dT.add(d);
            d.setLocation(DiceLocation.TABLE);
        }
        this.gameAssociated.setDiceBag(dB);
        this.gameAssociated.setDiceOnTable(dT);
        //bisogna considerare che lo dieToSwitch deve essere aggiunto allultimo giro nelle partite a 4 giocatoei.
    }

    /**
     * This method takes as input the list of players associated with the game and generates a new list containing the
     * same players but ordered as indicated in Sagrada's rules
     * @return list of swapped players
     */
    private List<Player> swapPlayerList(){
        List<Player> players = this.gameAssociated.getPlayersOrder();
        int swap = (gameAssociated.getActualRound()-1) % players.size();

        List<Player> toReturn = new ArrayList<>();
        for (int i = swap; i < players.size(); i++) toReturn.add(players.get(i));
        for (int i = 0; i < swap; i++ )toReturn.add(players.get(i));

        return toReturn;
    }

    /**
     * This method is called by the constructor and it returns the list of players ordered by turns for this round
     * @return list of players
     */
    private List generateTurnList(){
        List<Player> toReturn = swapPlayerList();

        for (int i = toReturn.size()- 1; i >= 0 ; i--)
            toReturn.add(toReturn.get(i));
        return toReturn;
    }

    /**
     * This method handles all the operations that take place when a turn is over
     */
    protected void nextTurn(){
        try {
            this.turnNumber++;
            this.gameController.toolCardController.activable.set(true);
            this.gameAssociated.setActualTurn(turnNumber);
            this.movableDice = 1;

            this.activePlayer = turnList.get(this.turnNumber);
            this.gameAssociated.setActivePlayer(turnList.get(this.turnNumber));

            //case if player has to skip turn due to toolcard #8
            if (activePlayer.hasToSkipNextTurn()){
                activePlayer.setSkipNextTurn(false);
                this.nextTurn();
                return;
            }

            //case if player is offline
            if (!this.gameAssociated.getPlayers().contains(this.activePlayer)){
                this.nextTurn();
                return;
            }


            this.workingPatternCard = this.activePlayer.getActivePatternCard();

            //Still active timer handling
            if (TimerHandler.checkTimer(timerID)) TimerHandler.stopTimer(this.timerID);
            this.timerID = TimerHandler.registerTimer(this, moveTimer);
            TimerHandler.startTimer(this.timerID);

        } catch (IndexOutOfBoundsException e){
            this.gameController.onNextRound();
            TimerHandler.stopTimer(this.timerID);
        }
    }

    /**
     * This method handles the update message
     * @param observable Observable
     * @param message UpdateMessage
     */
    private void handleUpdateMessage(Observable observable, UpdateMessage message){

        WhatToUpdate wtu = message.getWhatToUpdate();

        if (wtu.equals(WhatToUpdate.Pass)) this.nextTurn();
    }

    /**
     * This method handles the move dice message
     * @param observable Observable
     * @param mdm MoveDiceMessage
     */
    private synchronized void handleMoveDiceMessage(Observable observable, MoveDiceMessage mdm) {

        if (this.movableDice <= 0){
            ControllerCallbackMessage em = new ControllerCallbackMessage("You have already taken a die", LoggerPriority.ERROR);
            ((VirtualView)observable).controllerCallback(em);
            return;
        }

        if (mdm.getStartingLocation() == DiceLocation.TABLE) { //if the message handles taking action (From TABLE)

            //handle first put case
            if (this.workingPatternCard.getPlacedDice() == 0 &&
                    this.workingPatternCard.getCell(mdm.getEndingX(),mdm.getEndingY()).getNeighbourCells().size() > 3){
                ControllerCallbackMessage em = new ControllerCallbackMessage(CallbackMessageSubject.MoveNack, "First die has to be put in a outer cell", LoggerPriority.ERROR);
                ((VirtualView)observable).controllerCallback(em);
                return;
        }

            Die d = this.gameAssociated.getDiceOnTable().get(mdm.getTableCoordinate());
            d.setLocation(mdm.getEndingLocation()); //set the dice final location to the right type
            try {
                if (this.gameAssociated.getActualRound() == 1 && this.workingPatternCard.getPlacedDice() == 0) this.workingPatternCard.insertDice(d, mdm.getEndingX(), mdm.getEndingY(), true, true, false);
                else
                    this.workingPatternCard.insertDice(d, mdm.getEndingX(), mdm.getEndingY(), true, true, true);
                this.gameAssociated.getDiceOnTable().remove(d);
                this.movableDice--;
                ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.MoveAck, "Move received", LoggerPriority.NOTIFICATION);
                ((VirtualView) observable).controllerCallback(ccm);
                gameAssociated.triggerUpdate();

            } catch (NotValidInsertion e) {
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
                ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.MoveNack,"NOT_VALID_INSERTION: Not valid position", LoggerPriority.ERROR);
                ((VirtualView)observable).controllerCallback(ccm);

            } catch (NotEmptyWindowCellException e){
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
                ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.MoveNack, "NOT_VALID_INSERTION: Not empty cell", LoggerPriority.ERROR);
                ((VirtualView)observable).controllerCallback(ccm);
            }
        }

    }

    public void update(Observable observable, Object message){
        Message msg = (Message)message;
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION,":ROUND_HANDLER: Handling -> " + msg.getMessageType());

        if (! (((VirtualView)observable).getClient().getNickname().equals(this.activePlayer.getNickname()))){
            ControllerCallbackMessage ccm = new ControllerCallbackMessage("This is not your turn. Command ignored", LoggerPriority.ERROR);
            ((VirtualView)observable).controllerCallback(ccm);
            return;
        }

        switch (msg.getMessageType()){

            case "UpdateMessage":
                this.handleUpdateMessage(observable, (UpdateMessage)message);
                break;

            case "MoveDiceMessage":
                this.handleMoveDiceMessage(observable, (MoveDiceMessage)message);
                break;

            default: break;
        }
    }

    /**
     *  This method is called when the time ends
     */
    @Override
    public void timerDoneAction(){
        this.nextTurn();
    }
}


