package it.polimi.se2018.controller;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.*;

import java.util.List;
import java.util.Observable;

public class RoundHandler implements TimerInterface {

    private Game gameAssociated;
    private int turnNumber = 0;

    private long timerID;
    private long moveTimer;

    private final List<Player> turnList;

    public RoundHandler (Game game){
        this.gameAssociated = game;
        this.moveTimer = Server.getInstance().getDefaultMoveTimer();
        this.turnList = generateTurnList();

        this.gameAssociated.setActivePlayer(turnList.get(turnNumber));
        this.timerID = TimerHandler.registerTimer(this, moveTimer);
        TimerHandler.startTimer(this.timerID);
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

    public void update(Observable observable, Object message){
        Message msg = (Message)message;
        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":GAME_HANDLER: Handling -> " + msg.getMessageType());

        switch (msg.getMessageType()){

            default: break;
        }
    }

    @Override
    public void timerDoneAction(){
        this.nextTurn();
    }
}


