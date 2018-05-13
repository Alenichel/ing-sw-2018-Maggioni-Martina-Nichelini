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


    //private Player activePlayer;

    public Game(List<Player> players){
        this.players = players;
    }

    public void setDiceBag(ArrayList<Dice> diceBag) {
        this.diceBag = diceBag;
    }
    public void setDiceOnTable(ArrayList<Dice> diceOnTable) {
        this.diceOnTable = diceOnTable;
    }
    public void setPatternCards(ArrayList<WindowPatternCard> patternCards) {
        this.patternCards = patternCards;
    }
    public void setObjectiveCards(ArrayList<ObjectiveCard> objectiveCards) {
        this.objectiveCards = objectiveCards;
    }
    public void setToolCards(ArrayList<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public ArrayList<Dice> getDiceBag() {
        return diceBag;
    }
    public ArrayList<Dice> getDiceOnTable() {
        return diceOnTable;
    }
    public ArrayList<WindowPatternCard> getPatternCards() {
        return patternCards;
    }
    public ArrayList<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void addWindowPatternCard(WindowPatternCard windowPatternCard){
        this.patternCards.add(windowPatternCard);
    }

}
