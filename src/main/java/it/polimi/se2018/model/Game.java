package it.polimi.se2018.model;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.utils.TimerHandler;
import it.polimi.se2018.utils.TimerInterface;

import java.io.Serializable;
import java.util.*;

public class Game extends Observable implements Serializable, TimerInterface {
    private ArrayList<Dice> diceBag = new ArrayList<>();
    private ArrayList<Dice> diceOnTable = new ArrayList<>();
    private ArrayList<WindowPatternCard> patternCards = new ArrayList<>();
    private ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    private ArrayList<ToolCard> toolCards = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private boolean isStarted;
    private int currentRound = 0;
    private long timerID;
    private int timerSecondLeft = 0;

    private long matchMakingTimer;

    private GameController associatedGameController;

    public Game(){
        associatedGameController = new GameController(this);
        this.matchMakingTimer = (long)Server.getInstance().getDefaultMatchmakingTimer();
    }

    public boolean isStarted() {
        return isStarted;
    }

    public GameController getAssociatedGameController() {
        return associatedGameController;
    }

    public void setDiceBag(List<Dice> diceBag) {
        this.diceBag = (ArrayList<Dice>) diceBag;
    }
    public void setDiceOnTable(List<Dice> diceOnTable) {
        this.diceOnTable = (ArrayList<Dice>)diceOnTable;
    }
    public void setPatternCards(List<WindowPatternCard> patternCards) {
        this.patternCards = (ArrayList<WindowPatternCard>) patternCards;
    }
    public void setObjectiveCards(List<ObjectiveCard> objectiveCards) {
        this.objectiveCards = (ArrayList<ObjectiveCard>) objectiveCards;
    }
    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = (ArrayList<ToolCard>) toolCards;
    }
    public void setTimerSecondLeft(int second){
        this.timerSecondLeft = second;
        this.setChanged();
        UpdateMessage um = new UpdateMessage("TimeLeft");
        um.setStringMessage(String.valueOf(this.timerSecondLeft));
        this.notifyObservers(um);
    }
    public void setStarted(boolean started) throws GameException {
        if (this.isStarted) throw new GameException("Game already started");
        else {
            this.isStarted = started;
            UpdateMessage um = new UpdateMessage("GameStarted");
            um.setStringMessage("Game started");
            this.setChanged();
            this.notifyObservers(um);
        }
    }

    public List<Dice> getDiceBag() {
        return diceBag;
    }
    public List<Dice> getDiceOnTable() {
        return diceOnTable;
    }
    public List<WindowPatternCard> getPatternCards() {
        return patternCards;
    }
    public List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
    public List<ToolCard> getToolCards() {
        return toolCards;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public void addWindowPatternCard(WindowPatternCard windowPatternCard){
        this.patternCards.add(windowPatternCard);
    }
    public void addPlayer(Player player) throws GameException{
        if(!isStarted){
            if(this.players.size() > 3)
                throw new GameException("GameIsFull");
            else {
                this.players.add(player);
                UpdateMessage um = new UpdateMessage("NewPlayer");
                um.setStringMessage(player.getNickname() + " has just joined the game");
                this.setChanged();
                this.notifyObservers(um);
            }

            if (this.players.size() == 2){
                if (!TimerHandler.checkTimer(this.timerID)) {
                    this.timerID = TimerHandler.registerTimer(this, this.matchMakingTimer);
                    TimerHandler.startTimer(this.timerID);
                    UpdateMessage um = new UpdateMessage("NewTimer");
                    um.setStringMessage("A new timer has been initialized");
                    this.setChanged();
                    this.notifyObservers(um);
                }
            }
        }else{
            throw new GameException("AlreadyStartedGame");
        }
    }
    public void removePlayer(Player player){
        this.players.remove(player);
    }

    @Override
    public void timerDoneAction(){
        try {
            if (this.players.size() >= 2) this.setStarted(true);
            else {
                this.setChanged();
                UpdateMessage um = new UpdateMessage("Timer");
                um.setStringMessage("Timer done but not enough players connected");
                this.notifyObservers(um);
            }

        }catch (GameException e){
            e.printStackTrace();
        }
    }
}



