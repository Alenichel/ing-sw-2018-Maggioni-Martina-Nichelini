package it.polimi.se2018.controller;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.*;
import it.polimi.se2018.message.*;

import java.io.Serializable;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * This class implements GameController
 */
public class GameController implements Observer, Serializable, TimerInterface {
    private List<WindowPatternCard> initializedPatternCards = new ArrayList<>();

    private Server server;
    private ServerController serverController;
    public final Game gameAssociated;
    private GameSetupController gameSetupController;
    private transient final ToolCardController toolCardController;
    private transient RoundHandler roundHandler;

    private long timerID;
    private long matchMakingTimer;


    /**
     * Game controller constructor
     * @param game: associated game
     */
    public GameController(Game game){
        this.server = Server.getInstance();
        this.serverController = ServerController.getInstance();
        this.gameAssociated = game;

        this.matchMakingTimer = server.getDefaultMatchmakingTimer();

        this.gameSetupController = new GameSetupController(this.gameAssociated);
        this.toolCardController = new ToolCardController(this.gameAssociated);
    }

    /**
     * This method handles the connection operation. It's always called together with the method of the same name in ServerController.
     * With whom it divides actions and handles the operation only on it's associated model class.
     * @param player to be added to the game.
     */
    protected synchronized void connectPlayer(Player player) {

        if (player.getLastGameJoined() != null && this.server.getActiveGames().contains(player.getLastGameJoined())){ //handling the case in witch a Player reconnect and was previously connected
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


        if (this.gameAssociated.getPlayers().size() == 2) { //handling timer start
            if (!TimerHandler.checkTimer(this.timerID)) {
                this.timerID = TimerHandler.registerTimer(this, this.matchMakingTimer);
                TimerHandler.startTimer(this.timerID);
            }
        } else if (this.gameAssociated.getPlayers().size() == 4) { //handling game start
            if (TimerHandler.checkTimer(timerID)) TimerHandler.stopTimer(this.timerID);
            this.launchGame();
        }
    }

    /**
     * This method handles the disconnection operation
     * @param player to be removed from the game
     */
    private synchronized  void disconnectPlayer(Player player){
        gameAssociated.removePlayer(player);
        if (this.gameAssociated.getActivePlayer().equals(player)) this.roundHandler.nextTurn();
        player.setInGame(false);
        this.serverController.disconnectPlayer(player);

        if (gameAssociated.getPlayers().size() == 1){
            onGameEnd();
        }
    }

    /**
     * This method selects 4 random window pattern cards for the player to choose
     * @return window pattern cards
     */
    private List<WindowPatternCard> getRandomPatternCards (){
        int selectedIndex = 0;
        List<WindowPatternCard> toReturn = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 4; i++){
            selectedIndex = rand.nextInt(this.initializedPatternCards.size());
            toReturn.add(this.initializedPatternCards.get(selectedIndex));
            this.initializedPatternCards.remove(selectedIndex);
        }
        return toReturn;

    }

    /**
     * This method launches the game by setting the player's order, initializing GameSetupController and setting the
     * associated game to started
     */
    private void launchGame(){
        try {
            for (Player p : this.gameAssociated.getPlayers() )
                this.gameAssociated.getPlayersOrder().add(p);
            this.gameSetupController.initialize();
            this.gameAssociated.setStarted(true);

        }catch (GameException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
        }
    }

    /**
     * This method is called in the RoundHandler
     */
    protected void onNextRound(){
        this.gameAssociated.setActualRound(this.gameAssociated.getActualRound() + 1);
        roundHandler = new RoundHandler(gameAssociated);
    }

    /**
     * This method is called in GameSetupController
     */
    protected void onInitializationComplete(){
        gameSetupController = null;
        this.gameAssociated.setInitializationComplete(true);
        this.gameAssociated.setActualRound(1);
        this.roundHandler = new RoundHandler(gameAssociated);
    }

    /**
     * This method calculates the final score
     * @param player whose score is being calculated
     * @return the score
     */
    protected int calculateScore(Player player){
        int score = 0;
        int pocScore = 0;

        HashMap<String, Integer> scoreMap = player.getScores();

        for (PublicObjectiveCard poc : player.getLastGameJoined().getObjectiveCards()){
            pocScore += poc.scorePoint(player.getActivePatternCard());
        }
        score += pocScore;
        scoreMap.put("Public Objective Score", pocScore);

        score += player.getPrivateObjectiveCard().scorePoint();
        scoreMap.put("Private Objective Score", pocScore);

        int diceScore = (20 - player.getActivePatternCard().getPlacedDice());
        score -= diceScore;
        scoreMap.put("Dice Score", -diceScore);

        int favourTokensLeft = player.getActivePatternCard().getNumberOfFavorTokens();
        score += favourTokensLeft;
        scoreMap.put("Favour Tokens Left", favourTokensLeft);

        player.setScore(score);
        return score;
    }

    /**
     * This method is called in     GameController and RoundHandler
     */
    protected void onGameEnd(){

        Player topPlayer = null;
        int topScore = -100;


        // case when game ends with only a connected player.
        if (gameAssociated.getPlayers().size() == 1){
            topPlayer = gameAssociated.getPlayers().get(0);
        }

        // otherwise calculate score
        else {
            for (Player p : this.gameAssociated.getPlayers()) {
                int playerScore = calculateScore(p);
                if (playerScore > topScore) {
                    topScore = playerScore;
                    topPlayer = p;
                }
            }
        }

        //clean player from game setup references.
        for (Player p : gameAssociated.getPlayersOrder()) {
            p.setActivePatternCard(null);
            p.setSkipNextTurn(false);
            p.setScore(0);
            // p.assignObjectiveCard(null);
        }


        this.gameAssociated.setWinner(topPlayer);
        this.serverController.removeGame(this.gameAssociated);
        if (roundHandler != null) TimerHandler.stopTimer(roundHandler.timerID);
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Game " + this.gameAssociated.getName().toString() + " ended.");
        }

    /**
     * Round handler getter
     * @return round handler
     */
    protected RoundHandler getActiveRoundHandler(){
        return roundHandler;
    }

    /**
     * This method handles the update message
     * @param observable Observable
     * @param message UpdateMessage
     */
    private void handleUpdateMessage(Observable observable, UpdateMessage message){

        WhatToUpdate wtu = message.getWhatToUpdate();

        if (wtu.equals(WhatToUpdate.Pass)){
                if (!this.gameAssociated.isInitiliazationComplete()){
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("PatternCard has not been selected yet.", LoggerPriority.ERROR);
                    ((View)observable).controllerCallback(ccm);
                }
                else this.roundHandler.update(observable, message);
        }
    }

    /**
     * This method handles the connection message
     * @param observable Observable
     * @param message ConnectionMessage
     */
    private void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  ) {
                this.gameAssociated.addObserver((Observer)observable); //subscribe the view to the game
                this.connectPlayer(((VirtualView)observable).getClient()); //connect the player
                observable.addObserver(this); //subscribe gameController to the view
                observable.deleteObserver(ServerController.getInstance()); //unsubscribe ServerController from the view
                this.server.deleteObserver((Observer)observable); //unsiscribe view from Server.
                String string = gameAssociated.getName().toString() + " ---> There are " + gameAssociated.getPlayers().size()  + " player connected.";
                ControllerCallbackMessage ccm = new ControllerCallbackMessage(string,LoggerPriority.NOTIFICATION);
                ((View)observable).controllerCallback(ccm);
            }
        }
        else {
            observable.deleteObserver(this);
            this.gameAssociated.deleteObserver((Observer)observable);
            this.server.deleteObserver((Observer)observable);
            this.disconnectPlayer(message.getRequester());
        }
    }

    /**
     * This method handles the selection message
     * @param observable Observable
     * @param message SelectionMessage
     */
    private void handleSelectionMessage(Observable observable, SelectionMessage message){

        if (message.getSelected().equals("PatternCard") && gameSetupController != null){
            ControllerCallbackMessage ccm = new ControllerCallbackMessage("Selection acquired", LoggerPriority.NOTIFICATION);
            ((View)observable).controllerCallback(ccm);
            this.gameSetupController.update(observable, message);
        }
    }

    @Override
    public void update(Observable observable, Object msg){

        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, ":GAME_CONTROLLER( " + this.gameAssociated.getName().toString() + " ): Receveid -> " + ((Message)msg).getMessageType() );

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

            case "ReConnectionMessage":
                this.handleConnectionMessage(observable, (ConnectionMessage)msg);
                break;

            case "SelectionMessage":
                this.handleSelectionMessage(observable, (SelectionMessage)msg);
                break;

            case "MoveDiceMessage":
                this.roundHandler.update(observable, msg);
                break;

            case "ToolcardMessage":
                toolCardController.activateToolcard( (VirtualView)observable,(ToolCardMessage)msg);
                break;

            default: break;
        }
    }

    /**
     * This method is the one called when the timer is done.
     * It initializes all the game's related classes and sets the game to active
     */
    @Override
    public void timerDoneAction(){
        this.launchGame();
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Game started");
        serverController.resetGame();
    }
}
