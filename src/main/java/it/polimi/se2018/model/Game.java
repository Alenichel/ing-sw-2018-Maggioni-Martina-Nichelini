package it.polimi.se2018.model;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.enumeration.GameNames;


import java.io.Serializable;
import java.util.*;

public class Game extends Observable implements Serializable {
    private List<Dice> diceBag = new Vector<>();
    private List<Dice> diceOnTable = new Vector<>();
    private List<WindowPatternCard> patternCards = new Vector<>();
    private List<PublicObjectiveCard> objectiveCards = new Vector<>();
    private List<ToolCard> toolCards = new Vector<>();
    private List<Player> players = new Vector<>();
    private List<Player> playersOrder = new Vector<>();

    private RoundTrack roundTrack;
    private boolean isStarted;
    private boolean initiliazationComplete;
    private int actualRound = 0;
    private int actualTurn;
    private Player activePlayer = null;
    private int timerSecondsLeft;

    private HashMap scores = new HashMap<Player, Integer>();

    private GameController associatedGameController;

    private GameNames name;

    private Player winner = null;

    public Game(){
        associatedGameController = new GameController(this);
        this.roundTrack = new RoundTrack();

        Random random = new Random();
        name = GameNames.values()[random.nextInt(GameNames.values().length)];
    }

    public synchronized boolean isStarted() {
        return isStarted;
    }

    public synchronized GameController getAssociatedGameController() {
        return associatedGameController;
    }

    public synchronized void setDiceBag(List<Dice> diceBag) {
        this.diceBag = diceBag;
    }
    public synchronized void setDiceOnTable(List<Dice> diceOnTable) {
        this.diceOnTable = diceOnTable;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.DiceOnTable);
        um.setStringMessage("Some dice have been draft and added to the table");
        this.setChanged();
        this.notifyObservers(um);
    }
    public synchronized void setPatternCards(List<WindowPatternCard> patternCards) {
        this.patternCards = patternCards;
    }
    public synchronized void setObjectiveCards(List<PublicObjectiveCard> objectiveCards) {
        this.objectiveCards = objectiveCards;
    }
    public synchronized void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }
    public synchronized void setTimerSecondLeft(int second){
        this.timerSecondsLeft = second;
        this.setChanged();
        UpdateMessage um = new UpdateMessage(WhatToUpdate.TimeLeft);
        um.setStringMessage(String.valueOf(timerSecondsLeft));
        this.notifyObservers(um);
    }
    public synchronized void setStarted(boolean started) throws GameException {
        if (this.isStarted) throw new GameException("Game already started");
        else {
            if (this.players.size() > 1) {
                this.isStarted = started;
                UpdateMessage um = new UpdateMessage(WhatToUpdate.GameStarted);
                um.setStringMessage("Game started");
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
    }
    public synchronized void setInitializationComplete(boolean initializationComplete) {
        this.initiliazationComplete = initializationComplete;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.InitializationStatus);
        um.setStringMessage("Initialization complete");
        this.setChanged();
        this.notifyObservers(um);
    }
    public synchronized void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.ActivePlayer);
        um.setStringMessage("This is the turn of player:" + activePlayer.getNickname());
        this.setChanged();
        this.notifyObservers(um);
    }
    public synchronized void setActualTurn (int turn) { this.actualTurn = turn;}
    public synchronized void setActualRound(int actualRound) {
        this.actualRound = actualRound;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.NewRound);
        um.setStringMessage("This is round number: " + this.actualRound);
        this.setChanged();
        this.notifyObservers(um);
    }
    public synchronized void setWinner(Player winner) {
        this.winner = winner;

        UpdateMessage um = new UpdateMessage(WhatToUpdate.Winner);
        this.setChanged();
        this.notifyObservers(um);
    }
    public synchronized void setPlayersOrder(List<Player> playersOrder) {
        this.playersOrder = playersOrder;
    }
    public synchronized List<Dice> getDiceBag() {
        return diceBag;
    }
    public synchronized List<Dice> getDiceOnTable() {
        return diceOnTable;
    }
    public synchronized boolean isInitiliazationComplete() {
        return initiliazationComplete;
    }
    public synchronized int getTimerSecondsLeft() {
        return timerSecondsLeft;
    }
    public synchronized GameNames getName() {
        return name;
    }
    public synchronized Player getWinner() {
        return winner;
    }
    public synchronized List<WindowPatternCard> getPatternCards() {
        return patternCards;
    }
    public synchronized List<PublicObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
    public synchronized List<ToolCard> getToolCards() {
        return toolCards;
    }
    public synchronized List<Player> getPlayers() {
        return players;
    }
    public synchronized List<Player> getPlayersOrder() {
        return playersOrder;
    }
    public synchronized Map getScores() {
        return scores;
    }
    public synchronized Player getActivePlayer() {
        return activePlayer;
    }
    public synchronized RoundTrack getRoundTrack() {
        return roundTrack;
    }
    public synchronized int getActualRound() {
        return actualRound;
    }
    public synchronized int getActualTurn() {return actualTurn; }
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
    public synchronized void removePlayer(Player player){
        this.players.remove(player);
        UpdateMessage um = new UpdateMessage(WhatToUpdate.PlayerDisconnection);
        if (this.players.size() > 1) um.setStringMessage(player.getNickname() + " left the game");
        else um.setStringMessage(player.getNickname() + " left the game. You are the only human player left.");
        this.setChanged();
        this.notifyObservers(um);
    }
}



