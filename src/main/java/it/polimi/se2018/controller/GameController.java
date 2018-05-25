package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.utils.TimerHandler;
import it.polimi.se2018.utils.TimerInterface;
import it.polimi.se2018.view.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.*;

public class GameController implements Observer, Serializable, TimerInterface {
    private ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();

    private Server server;
    public final Game gameAssociated;
    private GameSetupController gameSetupController;
    private transient RoundHandler roundHandler;

    private long timerID;
    private long matchMakingTimer;


    public GameController(Game game){
        this.server = Server.getInstance();
        this.gameAssociated = game;

        this.matchMakingTimer = server.getDefaultMatchmakingTimer();

        this.gameSetupController = new GameSetupController(this.gameAssociated);
        //this.roundHandler = new RoundHandler(gameAssociated);
    }

    /**
     * This method handles connection operation. It's always called together with the method of the same name in ServerController.
     * With whom, it divides actions and handles operation only on it's associated model class.
     * @param player The player to add to the game.
     */
    private synchronized void connectPlayer(Player player) {

        try {
            gameAssociated.addPlayer(player);
        } catch (GameException e) {
            Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
        }
        player.setInGame(true); // set player status to true
        this.server.addPlayer(server.getInGamePlayers(), player); //add him to the list of inGame players
        player.setLastGameJoined(server.getCurrentGame()); //change last game joined param

        if (this.gameAssociated.getPlayers().size() == 2) {
            if (!TimerHandler.checkTimer(this.timerID)) {
                this.timerID = TimerHandler.registerTimer(this, this.matchMakingTimer);
                TimerHandler.startTimer(this.timerID);
            }
        } else if (this.gameAssociated.getPlayers().size() == 4) {
            try {
                this.gameAssociated.setStarted(true);
            } catch (GameException e) {
                Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
            }
        }
    }

    private List<WindowPatternCard> getRandomPatternCards (){
        int selectedIndex = 0;
        ArrayList<WindowPatternCard> toReturn = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 4; i++){
            selectedIndex = rand.nextInt(this.initializedPatternCards.size());
            toReturn.add(this.initializedPatternCards.get(selectedIndex));
            this.initializedPatternCards.remove(selectedIndex);
        }
        return toReturn;

    }

    private void onPatternCardSelection(Game game, SelectionMessage message) throws InvalidClassException {
        if (message.getChosenItem() instanceof WindowPatternCard ){
            Player targetPlayer = game.getPlayers().get(message.getPlayerNumber());
            targetPlayer.setActivePatternCard((WindowPatternCard)(message.getChosenItem()));
            game.addWindowPatternCard((WindowPatternCard) message.getChosenItem());
            //this.gameReference.notifyAll();
        } else {
            throw new InvalidClassException("Received: " + message.getChosenItem() + "but requested WindowPatternCard");
        }
    }

    private void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  ) {
                this.connectPlayer(message.getRequester());
                this.gameAssociated.addObserver((Observer)observable);
            }
        }
    }

    @Override
    public void update(Observable observable, Object msg){

        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":GAMECONTROLLER: Receveid -> " + ((Message)msg).getMessageType());

        switch(((Message)msg).getMessageType()){

            case "RequestMessage":
                RequestMessage rMsg = ((RequestMessage)msg);
                if (rMsg.getRequest().equalsIgnoreCase("PatternCardPool")){
                    ((VirtualView)observable).controllerCallback(new GiveMessage("PatternCardPool", this.getRandomPatternCards()));
                }
                else if (rMsg.getRequest().equals("PlayerInGame")){
                    ((VirtualView) observable).controllerCallback(new GiveMessage("PlayerInGame", gameAssociated.getPlayers()));
                }
                break;

            case "SetupMessage":
                gameSetupController.initialize();
                break;

            case "ConnectionMessage":
                this.handleConnectionMessage(observable, (ConnectionMessage)msg);
                break;

            default: break;
        }
    }

    /**
     * This method is the one called when timer is done. It initializes all the game's related class and set the game to active.
     */
    @Override
    public void timerDoneAction(){
        try {
            this.gameSetupController.initialize();
            this.gameAssociated.setStarted(true);

        }catch (GameException e){
            Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
        }
    }

}
