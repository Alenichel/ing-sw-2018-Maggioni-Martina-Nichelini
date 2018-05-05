package it.polimi.se2018;
import java.util.ArrayList;
import java.util.Random;


public class Game {

    private int id;
    private ArrayList<Dice> diceBag = new ArrayList<>();
    private ArrayList<Dice> diceOnTable = new ArrayList<>();
    private ArrayList<WindowPatternCard> patternCards = new ArrayList<>();
    private ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    private ArrayList<ToolCard> toolCards = new ArrayList<>();

    private ArrayList<Player> roundOrderPlayers = new ArrayList<>(); //turn handling
    private Player activePlayer; // turn handling

    public Game() {
        this.id = this.hashCode();

    }

    public void addPlayerToGame(Player player){
        roundOrderPlayers.add(player);
    }

    public void removePlayerFromGame(Player player){
        roundOrderPlayers.remove(player);
    }

    public Player getActivePlayer(){
        return activePlayer;
    }

    public void setActivePlayer(Player player){
        this.activePlayer = player;
    }

    public void setRoundOrderPlayers(){
        //to be implemented
    }

    public void draftDice(int nOfDice){
        Random randomizer = new Random();
        for (int i = 0; i<nOfDice; i++) {
            Dice dice = diceBag.get(randomizer.nextInt(diceBag.size()));
            diceBag.remove(dice);
            diceOnTable.add(dice);
        }
    }
}


