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

public class RoundHandler implements TimerInterface {

    private Game gameAssociated;
    private int turnNumber = 0;

    private long timerID;
    private long moveTimer;
    private Random rand = new Random();

    private final List<Player> turnList;

    public RoundHandler (Game game){
        this.gameAssociated = game;
        this.moveTimer = Server.getInstance().getDefaultMoveTimer();
        this.turnList = generateTurnList();

        this.gameAssociated.setActivePlayer(turnList.get(turnNumber)); //set the first player as active player
        this.extractDice();
        this.timerID = TimerHandler.registerTimer(this, moveTimer); //register new turn timer
        TimerHandler.startTimer(this.timerID);
    }

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

    private List generateTurnList(){
        List<Player> toReturn = this.gameAssociated.getPlayers();
         for (int i = this.gameAssociated.getPlayers().size()- 1; i >= 0 ; i--){
             toReturn.add(this.gameAssociated.getPlayers().get(i));
         }
         return toReturn;
    }

    private void nextTurn(){
        this.turnNumber++;
        this.gameAssociated.setActivePlayer(turnList.get(this.turnNumber));
        if (TimerHandler.checkTimer(timerID)) TimerHandler.stopTimer(this.timerID);
        this.timerID = TimerHandler.registerTimer(this, moveTimer);
        TimerHandler.startTimer(this.timerID);
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


