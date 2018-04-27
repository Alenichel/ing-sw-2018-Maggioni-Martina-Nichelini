package it.polimi.se2018;
import java.util.ArrayList;

public class Game {

    private int id;
    private ArrayList<Dice> bag = new ArrayList<>();
    private ArrayList<WindowPatternCard> patternCards = new ArrayList<>();
    private ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    private ArrayList<ToolCard> toolCards = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    public Game() {
        this.id = this.hashCode();

    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }
}


