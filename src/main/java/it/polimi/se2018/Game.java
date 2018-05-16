package it.polimi.se2018;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Dice> diceBag = new ArrayList<>();
    private ArrayList<Dice> diceOnTable = new ArrayList<>();
    private ArrayList<WindowPatternCard> patternCards = new ArrayList<>();
    private ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    private ArrayList<ToolCard> toolCards = new ArrayList<>();
    private final List<Player> players;

    private int nOfPlayers;

    private int currentRound = 0;

    //private Player activePlayer;

    public Game(List<Player> players){
        this.players = players;
        this.nOfPlayers = this.players.size();
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

}
