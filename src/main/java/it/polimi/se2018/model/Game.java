package it.polimi.se2018.model;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.UpdateMessage;

import java.io.Serializable;
import java.util.*;

public class Game extends Observable implements Serializable {
    private ArrayList<Dice> diceBag = new ArrayList<>();
    private ArrayList<Dice> diceOnTable = new ArrayList<>();
    private ArrayList<WindowPatternCard> patternCards = new ArrayList<>();
    private ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    private ArrayList<ToolCard> toolCards = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private boolean isStarted;
    private int currentRound = 0;


    private GameController associatedGameController;



    public Game(){
        associatedGameController = new GameController(this);
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
    public void setStarted(boolean started) throws GameException {
        if (this.isStarted) throw new GameException("Game already started");
        else {
            this.isStarted = started;
            UpdateMessage um = new UpdateMessage("GameStarted");
            um.setStringMessage("Game started");
            this.setChanged();
            this.notifyObservers(um);
            //timer.cancel();
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

            if (this.players.size() > 1){
                //timer.schedule(this.launchTask, (long)Server.getInstance().getDefaultMatchmakingTimer() );
                UpdateMessage um = new UpdateMessage("NewTimer");
                um.setStringMessage("A new timer has been initialized");
                this.setChanged();
                this.notifyObservers(um);
            }
        }else{
            throw new GameException("AlreadyStartedGame");
        }
    }
}



