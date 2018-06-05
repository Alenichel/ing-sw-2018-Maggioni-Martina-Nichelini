package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.*;
import it.polimi.se2018.message.*;

import java.io.Serializable;
import java.util.*;

public class GameController implements Observer, Serializable, TimerInterface {
    private ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();

    private Server server;
    private ServerController serverController;
    public final Game gameAssociated;
    private GameSetupController gameSetupController;
    private transient RoundHandler roundHandler;

    private long timerID;
    private long matchMakingTimer;


    public GameController(Game game){
        this.server = Server.getInstance();
        this.serverController = ServerController.getInstance();
        this.gameAssociated = game;

        this.matchMakingTimer = server.getDefaultMatchmakingTimer();

        this.gameSetupController = new GameSetupController(this.gameAssociated);
    }

    /**
     * This method handles connection operation. It's always called together with the method of the same name in ServerController.
     * With whom, it divides actions and handles operation only on it's associated model class.
     * @param player The player to add to the game.
     */
    protected synchronized void connectPlayer(Player player) {

        if (player.getLastGameJoined() != null && this.server.getActiveGames().contains(player.getLastGameJoined())){ //handling the case in witch a Player reconnect and was previosly reconnected
            player.setInGame(true);
            try {
                player.getLastGameJoined().addPlayer(player);
            } catch (GameException e ) {
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
            }
            return;
        }


        try {
            gameAssociated.addPlayer(player);
        } catch (GameException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR,e.toString());
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
            if (TimerHandler.checkTimer(timerID)) TimerHandler.stopTimer(this.timerID);
            this.launchGame();
        }
    }

    private synchronized  void disconnectPlayer(Player player){
        gameAssociated.removePlayer(player);
        player.setInGame(false);
        this.server.removePlayer(server.getInGamePlayers(), player);
        this.serverController.disconnectPlayer(player);

        if (gameAssociated.getPlayers().size() == 1){
            onGameEnd();
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

    private void launchGame(){
        try {
            this.gameSetupController.initialize();
            this.gameAssociated.setStarted(true);

        }catch (GameException e){
            Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
        }
    }

    protected void onNextRound(){
        this.gameAssociated.setActualRound(this.gameAssociated.getActualRound() + 1);
        roundHandler = new RoundHandler(gameAssociated);
    }

    protected void onInitializationComplete(){
        gameSetupController = null;
        this.gameAssociated.setInitializationComplete(true);
        this.gameAssociated.setActualRound(1);
        this.roundHandler = new RoundHandler(gameAssociated);
    }

    protected int calculateScore(Player player){
        int score = 0;

        for (PublicObjectiveCard poc : player.getLastGameJoined().getObjectiveCards()){
            score += poc.scorePoint(player.getActivePatternCard());
        }

        score -= (20 - player.getActivePatternCard().getPlacedDice());
        player.setScore(score);
        return score;
    }

    protected void onGameEnd(){

        Player topPlayer = null;
        int topScore = -100;

        if (gameAssociated.getPlayers().size() == 1){
            topPlayer = gameAssociated.getPlayers().get(0);
        }

        else {
            for (Player p : this.gameAssociated.getPlayers()) {
                int playerScore = calculateScore(p);
                if (playerScore > topScore) {
                    topScore = playerScore;
                    topPlayer = p;
                }
            }
        }

        this.gameAssociated.setWinner(topPlayer);
        TimerHandler.stopTimer(this.timerID);
        this.serverController.removeGame(this.gameAssociated);
        }

    private void handleUpdateMessage(Observable observable, UpdateMessage message){

        switch (message.getWhatToUpdate()){

            case "Pass":
                if (!this.gameAssociated.isInitiliazationComplete()){
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("PatternCard has not been selected yet.", LoggerPriority.ERROR);
                    ((View)observable).controllerCallback(ccm);
                    break;
                }
                this.roundHandler.update(observable, message);
                break;

            default: break;
        }
    }

    private void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  ) {
                this.connectPlayer(message.getRequester()); //connect the player
                this.gameAssociated.addObserver((Observer)observable); //subscribe the player to the game
                observable.addObserver(this); //subscribe gameController to the view
                observable.deleteObserver(ServerController.getInstance()); //unsubscribe ServerController from the view
                String string = "You joined to " + gameAssociated.getName().toString() + "game. There are " + gameAssociated.getPlayers().size()  + " player connected.";
                ControllerCallbackMessage ccm = new ControllerCallbackMessage(string,LoggerPriority.NOTIFICATION);
                ((View)observable).controllerCallback(ccm);
            }
        }
        else {
                this.disconnectPlayer(message.getRequester());
        }
    }

    private void handleSelectionMessage(Observable observable, SelectionMessage message){

        if (message.getSelected().equals("PatternCard") && gameSetupController != null){
            ControllerCallbackMessage ccm = new ControllerCallbackMessage("Selection acquired", LoggerPriority.NOTIFICATION);
            ((View)observable).controllerCallback(ccm);
            this.gameSetupController.update(observable, message);
        }
    }


    @Override
    public void update(Observable observable, Object msg){

        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":GAME_CONTROLLER( " + this.gameAssociated.getName().toString() + " ): Receveid -> " + ((Message)msg).getMessageType() );

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

            case "UpdateMessage":
                this.handleUpdateMessage(observable, (UpdateMessage)msg);
                break;

            case "ConnectionMessage":
                this.handleConnectionMessage(observable, (ConnectionMessage)msg);
                break;

            case "SelectionMessage":
                this.handleSelectionMessage(observable, (SelectionMessage)msg);
                break;

            case "MoveDiceMessage":
                ControllerCallbackMessage ccm = new ControllerCallbackMessage("Move received", LoggerPriority.NOTIFICATION);
                ((VirtualView) observable).controllerCallback(ccm);
                this.roundHandler.update(observable, msg);
                break;

            default: break;
        }
    }

    /**
     * This method is the one called when timer is done. It initializes all the game's related class and set the game to active.
     */
    @Override
    public void timerDoneAction(){
        this.launchGame();
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Game started");
        serverController.resetGame();
    }
}
