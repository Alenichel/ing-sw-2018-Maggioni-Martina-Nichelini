package it.polimi.se2018.controller;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * This controller class handles all the operations that happen during a round. It's not included in a complete Observer Pattern
 * but it handles the update at the same way of main GameController class trough Update method.
 * This class implements the Timer Interface since it has to handle turn timer. timerDoneAction method is called when the time ends
 */
public class RoundHandler implements TimerInterface {

    private Game gameAssociated;
    private GameController gameController;
    private int turnNumber = 0;

    private long timerID;
    private long moveTimer;
    private Random rand = new Random();

    private final List<Player> turnList;

    public RoundHandler (Game game){
        this.gameAssociated = game;
        this.gameController = gameAssociated.getAssociatedGameController();
        this.moveTimer = Server.getInstance().getDefaultMoveTimer();
        this.turnList = generateTurnList();

        this.gameAssociated.setActivePlayer(turnList.get(turnNumber)); //set the first player as active player
        this.extractDice();
        this.timerID = TimerHandler.registerTimer(this, moveTimer); //register new turn timer
        TimerHandler.startTimer(this.timerID);
    }

    /**
     * This method extract dice from Dice Bag and put them on the table. Ii's called only once for round and it is called
     * in the constructor.
     */
    private void extractDice(){
        int nOfPlayers = this.gameAssociated.getPlayers().size();
        int diceToExtract = nOfPlayers * 2 + 1; //See Sagrada's rules.
        List<Dice> dB = this.gameAssociated.getDiceBag();
        List<Dice> dT = this.gameAssociated.getDiceOnTable();

        for (int i = 0; i < diceToExtract; i++){
            Dice d = dB.get(rand.nextInt(dB.size()));
            dB.remove(d);
            dT.add(d);
            d.setLocation(DiceLocation.TABLE);
        }
        this.gameAssociated.setDiceBag(dB);
        this.gameAssociated.setDiceOnTable(dT);
    }

    /**
     * This method take as input the list of players associated with the game and generate a new list containg the same
     * players but ordered as indicated in Sagrada's rules.
     * @return List of swaped players.
     */
    private List<Player> swapPlayerList(){
        List<Player> players = this.gameAssociated.getPlayers();
        int swap = (gameAssociated.getActualRound()-1) % players.size();

        List<Player> toReturn = new ArrayList<>();
        for (int i = swap; i < players.size(); i++) toReturn.add(players.get(i));
        for (int i = 0; i < swap; i++ )toReturn.add(players.get(i));

        return toReturn;
    }

    /**
     * This method is called by the constructor and it returns the list of players ordered by turns for this round.
     * @return List of player
     */
    private List generateTurnList(){
        List<Player> toReturn = swapPlayerList();

        for (int i = toReturn.size()- 1; i >= 0 ; i--)
            toReturn.add(toReturn.get(i));
        return toReturn;
    }

    /**
     * This method handles all the operation that take place when a turn is over.
     */
    private void nextTurn(){
        try {
            this.turnNumber++;
            this.gameAssociated.setActivePlayer(turnList.get(this.turnNumber));
            if (TimerHandler.checkTimer(timerID)) TimerHandler.stopTimer(this.timerID);
            this.timerID = TimerHandler.registerTimer(this, moveTimer);
            TimerHandler.startTimer(this.timerID);
        } catch (IndexOutOfBoundsException e){
            UpdateMessage um = new UpdateMessage("NextRound");
            gameController.update(null, um);
        }
    }

    private void handleUpdateMessage(Observable observable, UpdateMessage message){

        switch (message.getWhatToUpdate()){

            case "Pass":
                this.nextTurn();
                break;

            default:  break;
        }
    }

    private void handleMoveDiceMessage(Observable observable, MoveDiceMessage mdm) {

        if (mdm.getStartingLocation() == DiceLocation.TABLE) {
            Dice d = this.gameAssociated.getDiceOnTable().get(mdm.getTableCoordinate());
            d.setLocation(mdm.getEndingLocation());
            try {
                this.gameAssociated.getActivePlayer().getActivePatternCard().insertDice(d, mdm.getEndingX(), mdm.getEndingY(), true, true, true);
                List<Dice> diceOnTable = this.gameAssociated.getDiceOnTable();
                diceOnTable.remove(d);
                this.gameAssociated.setDiceOnTable(diceOnTable);
            } catch (NotEmptyWindowCellException | NotValidInsertion e) {
                Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
                ControllerCallbackMessage ccm = new ControllerCallbackMessage("NOT_VALID_INSERTION: Not valid position");
                ((VirtualView)observable).controllerCallback(ccm);
            }
        }

    }

    public void update(Observable observable, Object message){
        Message msg = (Message)message;
        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":ROUND_HANDLER: Handling -> " + msg.getMessageType());

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

    @Override
    public void timerDoneAction(){
        this.nextTurn();
    }
}


