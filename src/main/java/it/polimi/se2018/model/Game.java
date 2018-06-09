package it.polimi.se2018.model;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.message.WhatToUpdate;
import it.polimi.se2018.utils.GameNames;


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

    public boolean isStarted() {
        return isStarted;
    }

    public GameController getAssociatedGameController() {
        return associatedGameController;
    }

    public void setDiceBag(List<Dice> diceBag) {
        this.diceBag = diceBag;
    }
    public void setDiceOnTable(List<Dice> diceOnTable) {
        this.diceOnTable = diceOnTable;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.DiceOnTable);
        um.setStringMessage("Some dice have been draft and added to the table");
        this.setChanged();
        this.notifyObservers(um);
    }
    public void setPatternCards(List<WindowPatternCard> patternCards) {
        this.patternCards = patternCards;
    }
    public void setObjectiveCards(List<PublicObjectiveCard> objectiveCards) {
        this.objectiveCards = objectiveCards;
    }
    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }
    public void setTimerSecondLeft(int second){
        this.timerSecondsLeft = second;
        this.setChanged();
        UpdateMessage um = new UpdateMessage(WhatToUpdate.TimeLeft);
        um.setStringMessage(String.valueOf(timerSecondsLeft));
        this.notifyObservers(um);
    }
    public void setStarted(boolean started) throws GameException {
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
    public void setInitializationComplete(boolean initializationComplete) {
        this.initiliazationComplete = initializationComplete;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.InitializationStatus);
        um.setStringMessage("Initialization complete");
        this.setChanged();
        this.notifyObservers(um);
    }
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.ActivePlayer);
        um.setStringMessage("This is the turn of player:" + activePlayer.getNickname());
        this.setChanged();
        this.notifyObservers(um);
    }
    public void setActualRound(int actualRound) {
        this.actualRound = actualRound;
        UpdateMessage um = new UpdateMessage(WhatToUpdate.NewRound);
        um.setStringMessage("This is round number: " + this.actualRound);
        this.setChanged();
        this.notifyObservers(um);
    }
    public void setWinner(Player winner) {
        this.winner = winner;

        UpdateMessage um = new UpdateMessage(WhatToUpdate.Winner);
        this.setChanged();
        this.notifyObservers(um);
    }
    public void setPlayersOrder(List<Player> playersOrder) {
        this.playersOrder = playersOrder;
    }

    public List<Dice> getDiceBag() {
        return diceBag;
    }
    public List<Dice> getDiceOnTable() {
        return diceOnTable;
    }
    public boolean isInitiliazationComplete() {
        return initiliazationComplete;
    }
    public int getTimerSecondsLeft() {
        return timerSecondsLeft;
    }
    public GameNames getName() {
        return name;
    }
    public Player getWinner() {
        return winner;
    }
    public List<WindowPatternCard> getPatternCards() {
        return patternCards;
    }
    public List<PublicObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
    public List<ToolCard> getToolCards() {
        return toolCards;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public List<Player> getPlayersOrder() {
        return playersOrder;
    }
    public HashMap getScores() {
        return scores;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public int getActualRound() {
        return actualRound;
    }

    public void addPlayer(Player player) throws GameException{
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
    public void removePlayer(Player player){
        this.players.remove(player);
        UpdateMessage um = new UpdateMessage(WhatToUpdate.PlayerDisconnection);
        if (this.players.size() > 1) um.setStringMessage(player.getNickname() + " left the game");
        else um.setStringMessage(player.getNickname() + " left the game. You are the only human player left.");
        this.setChanged();
        this.notifyObservers(um);
    }


}



