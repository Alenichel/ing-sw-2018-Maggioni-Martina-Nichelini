package it.polimi.se2018.model;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.enumeration.GameNames;


import java.io.Serializable;
import java.util.*;

/**
 * This class implements the model side of the game
 */
public class Game extends Observable implements Serializable {
    private List<Die> diceBag = new Vector<>();
    private List<Die> diceOnTable = new Vector<>();
    private List<WindowPatternCard> patternCards = new Vector<>();
    private List<PublicObjectiveCard> objectiveCards = new Vector<>();
    private List<ToolCard> toolCards = new Vector<>();
    private List<Player> players = new Vector<>();
    private List<Player> playersOrder = new Vector<>();
    private Die dieForSwitch;
    private RoundTrack roundTrack;
    private boolean isStarted;
    private boolean initializationComplete;
    private int actualRound = 0;
    private int actualTurn;
    private Player activePlayer = null;
    private int timerSecondsLeft;
    private HashMap scores = new HashMap<Player, Integer>();
    private GameController associatedGameController;
    private GameNames name;
    private Player winner = null;

    /**
     * Game's constructor
     */
    public Game(){
        associatedGameController = new GameController(this);
        this.roundTrack = new RoundTrack();

        Random random = new Random();
        name = GameNames.values()[random.nextInt(GameNames.values().length)];
    }

    /**
     * Boolean isStarted
     * @return true if the game is started, false otherwise
     */
    public synchronized boolean isStarted() {
        return isStarted;
    }

    /**
     * Associated game controller getter
     * @return associated game controller
     */
    public synchronized GameController getAssociatedGameController() {
        return associatedGameController;
    }

    /**
     * Die bag setter
     * @param diceBag list of dice in the bag
     */
    public synchronized void setDiceBag(List<Die> diceBag) {
        this.diceBag = diceBag;
    }

    /**
     * Die on table setter
     * @param diceOnTable list of dice on the table
     */
    public synchronized void setDiceOnTable(List<Die> diceOnTable) {
        this.diceOnTable = diceOnTable;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.DiceOnTable);
        um.setStringMessage("Some dice have been draft and added to the table");
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Window Pattern cards setter
     * @param patternCards list of window pattern cards
     */
    public synchronized void setPatternCards(List<WindowPatternCard> patternCards) {
        this.patternCards = patternCards;
    }

    /**
     * Public objective cards setter
     * @param objectiveCards list of public objective cards
     */
    public synchronized void setObjectiveCards(List<PublicObjectiveCard> objectiveCards) {
        this.objectiveCards = objectiveCards;
    }

    /**
     * Tool cards setter
     * @param toolCards list of tool cards
     */
    public synchronized void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    /**
     * Timer setter
     * @param second seconds left
     */
    public synchronized void setTimerSecondLeft(int second){
        this.timerSecondsLeft = second;
        this.setChanged();
        UpdateMessage um = new UpdateMessage(WhatToUpdate.TimeLeft);
        um.setStringMessage(String.valueOf(timerSecondsLeft));
        this.notifyObservers(um);
    }

    /**
     * Game started setter
     * @param started true if the game has started, false otherwise
     * @throws GameException if the game has already started
     */
    public synchronized void setStarted(boolean started) throws GameException {
        if (this.isStarted) throw new GameException("Game already started");

        if (started){
            this.isStarted = started;
            UpdateMessage um = new UpdateMessage(WhatToUpdate.GameStarted);
            um.setStringMessage("Game"+ this.name +"started");
            this.setChanged();
            this.notifyObservers(um);
        }
        else{
            UpdateMessage um = new UpdateMessage(WhatToUpdate.Timer);
            um.setStringMessage("Timer done but not enough players connected");
            this.setChanged();
            this.notifyObservers(um);
            }
    }

    /**
     * Initialization complete setter
     * @param initializationComplete true if it's complete
     */
    public synchronized void setInitializationComplete(boolean initializationComplete) {
        this.initializationComplete = initializationComplete;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.InitializationStatus);
        um.setStringMessage("Initialization complete");
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Active player setter
     * @param activePlayer active player during the current round
     */
    public synchronized void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.ActivePlayer);
        um.setStringMessage("This is the turn of player:" + activePlayer.getNickname());
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Active player setter
     * @param turn the new active turn index
     */
    public synchronized void setActualTurn (int turn) { this.actualTurn = turn;}

    /**
     * Actual round setter
     * @param actualRound current round
     */
    public synchronized void setActualRound(int actualRound) {
        this.actualRound = actualRound;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.NewRound);
        um.setStringMessage("This is round number: " + this.actualRound);
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Winner setter
     * @param winner player with the higher score
     */
    public synchronized void setWinner(Player winner) {
        this.winner = winner;

        UpdateMessage um = new UpdateMessage(WhatToUpdate.Winner);
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Players order setter
     * @param playersOrder list of players
     */
    public synchronized void setPlayersOrder(List<Player> playersOrder) {
        this.playersOrder = playersOrder;
    }

    /**
     * Die for switch setter: it sets a random die from the dice bag
     */
    public synchronized void setDieForSwitch() {
        Random random = new Random();
        Die randomDie = diceBag.get(random.nextInt(diceBag.size()));
        this.dieForSwitch = randomDie;
    }

    /**
     * Die for switch getter
     * @return die for switch
     */
    public synchronized Die getDieForSwitch() {
        return dieForSwitch;
    }

    /**
     * Die on table getter
     * @return dice on table
     */
    public synchronized List<Die> getDiceBag() {
        return diceBag;
    }

    /**
     * Boolean is initialization complete
     * @return true if it's complete
     */
    public synchronized List<Die> getDiceOnTable() {
        return diceOnTable;
    }

    /**
     * Boolean is initialization complete
     * @return true if it's complete
     */
    public synchronized boolean isInitializationComplete() {
        return initializationComplete;
    }

    /**
     * Timer getter
     * @return timer seconds left
     */
    public synchronized int getTimerSecondsLeft() {
        return timerSecondsLeft;
    }

    /**
     * Game name getter
     * @return game name (ANewHope, TheBattleOfTheHeroes, TheRideOfRohirrim, TheHelmDitchBattle, MountDoomBattle or IdesOfMarch)
     */
    public synchronized GameNames getName() {
        return name;
    }

    /**
     * Winner getter
     * @return player who won the game
     */
    public synchronized Player getWinner() {
        return winner;
    }

    /**
     * Window pattern cards getter
     * @return list of window pattern cards
     */
    public synchronized List<WindowPatternCard> getPatternCards() {
        return patternCards;
    }

    /**
     * Public objective cards getter
     * @return list of public objective cards
     */
    public synchronized List<PublicObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * Tool cards getter
     * @return list of tool cards
     */
    public synchronized List<ToolCard> getToolCards() {
        return toolCards;
    }

    /**
     * Players getter
     * @return list of players
     */
    public synchronized List<Player> getPlayers() {
        return players;
    }

    /**
     * Players order getter
     * @return players order
     */
    public synchronized List<Player> getPlayersOrder() {
        return playersOrder;
    }

    /**
     * Scores getter
     * @return scores
     */
    public synchronized Map getScores() {
        return scores;
    }

    /**
     * Active player getter
     * @return active player
     */
    public synchronized Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Round track getter
     * @return round track
     */
    public synchronized RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Current round getter
     * @return current round
     */
    public synchronized int getActualRound() {
        return actualRound;
    }

    /**
     * Current turn getter
     * @return current turn
     */
    public synchronized int getActualTurn() {return actualTurn; }

    /**
     * This method adds a player to the game or re-adds a player to his last joined game
     * @param player mentioned above
     * @throws GameException when the game already has 4 players or when the game is started
     */
    public synchronized void addPlayer(Player player) throws GameException{
        if(!isStarted){
            if(this.players.size() > 3)
                throw new GameException("GameIsFull");
            else {
                this.players.add(player);
                UpdateMessage um = new UpdateMessage(WhatToUpdate.NewPlayer);
                um.setStringMessage(player.getNickname() + " has just joined the game");
                this.setChanged();
                this.notifyObservers(um);
            }

        }
        else if (player.getLastGameJoined().equals(this)){
            this.players.add(player);
            UpdateMessage um = new UpdateMessage(WhatToUpdate.NewPlayer);
            um.setStringMessage(player.getNickname() + " rejoined the game");
            this.setChanged();
            this.notifyObservers(um);
        }
        else{
            throw new GameException("AlreadyStartedGame");
        }
    }

    /**
     * This method removes a player from the game he was in
     * @param player mentioned above
     */
    public synchronized void removePlayer(Player player){
        this.players.remove(player);
        UpdateMessage um = new UpdateMessage(WhatToUpdate.PlayerDisconnection);
        if (this.players.size() > 1) um.setStringMessage(player.getNickname() + " left the game");
        else um.setStringMessage(player.getNickname() + " left the game. You are the only human player left.");
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * This method is called when it's necessary to manual trigger an update.
     */
    public synchronized void triggerUpdate(){
        this.setChanged();
        UpdateMessage um = new UpdateMessage(WhatToUpdate.ToolCardUpdate);
        um.setStringMessage("ToolCard Update");
        this.notifyObservers(um);
    }
}