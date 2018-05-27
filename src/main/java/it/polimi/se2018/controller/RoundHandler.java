package it.polimi.se2018.controller;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.*;

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

    public void update(Observable observable, Object message){
        Message msg = (Message)message;
        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":ROUND_HANDLER: Handling -> " + msg.getMessageType());

        switch (msg.getMessageType()){

            case "UpdateMessage":
                this.handleUpdateMessage(observable, (UpdateMessage)message);
                break;

            default: break;
        }
    }

    @Override
    public void timerDoneAction(){
        this.nextTurn();
    }
}


