package it.polimi.se2018;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Room class is the class that represents the canonical room where online game are setupped up.
 */
public class Room extends java.util.Observable{

    private List<Observer> observers = new ArrayList<>();

    private String roomName;
    private boolean singlePlayerMode;
    private int maxNOfPlayers;
    private int numberOfConnectedPlayer;
    private ArrayList <Player> listOfConnectedPlayer = new ArrayList<>();
    private Game gameAssociated;
    private GameController gameControllerAssociated;
    private ArrayList <Player> disconnectedClients = new ArrayList<>();

    /**
     * Class constructor
     * @param name Name to assign to the room
     * @param admin Player to be recognized as admin in room handling operation
     * @param singlePlayer True if the game is a single player game, False if is a standard multiplayer game
     */
    public Room(String name, Player admin, Boolean singlePlayer){
        this.roomName = name;
        this.singlePlayerMode = singlePlayer;
        this.maxNOfPlayers = singlePlayerMode ? 1 : 4;
        this.numberOfConnectedPlayer = 1;
        this.gameAssociated = null; //no game associated at room initialization
        this.gameControllerAssociated = null; //no gameController associated at room initialization
        listOfConnectedPlayer.add(admin);
    }

    /**
     * Adds player to the list of player
     * @author: Alessandro Nichelini
     * @param player Player to be added
     */
    public void addPlayer(Player player){
        if (numberOfConnectedPlayer + 1 > maxNOfPlayers) throw new IndexOutOfBoundsException("The room is full");

        listOfConnectedPlayer.add(player);
        numberOfConnectedPlayer = numberOfConnectedPlayer + 1;
        this.notifyObservers();
    }

    /**
     * Removes player from list of player
     * @author: Alessandro Nichelini
     * @param player Player to be removed
     */
    public void removePlayer(Player player){
        listOfConnectedPlayer.remove(player);
        numberOfConnectedPlayer = numberOfConnectedPlayer - 1;
        this.notifyObservers();
    }

    /**
     * Returns list of players that are currently connected to the room
     * @author: Alessandro Nichelini
     * @return List of player
     */
    public List<Player> getListOfConnectedPlayers() {
        return listOfConnectedPlayer;
    }

    /**
     * Adds player to the list of disconnected players
     * @author: Alessandro Nichelini
     * @param player Player to be added to the list
     */
    public void addDisconnectedClient(Player player){
        disconnectedClients.add(player);
        this.notifyObservers();
    }

    /**
     * Returns list of player that previosly disconeccted from the game associated with the room
     * @author: Alessandro Nichelini
     * @return List of player
     */
    public List<Player> getListOfDisconnectedClients(){
        return disconnectedClients;
    }

    /**
     * Returns True if the player is on the list of disconnected player, otherwise it returns False
     * @author: Alessandro Nichelini
     * @param player Player it's needed to be checked.
     * @return boolean
     */
    public boolean isADisconnectedClient(Player player){
        return this.disconnectedClients.contains(player);
    }

    /**
     * Associates a class game to this class
     * @author: Alessandro Nichelini
     * @param game Game class to be associated
     */
    public void setGameAssociated(Game game){
        this.gameAssociated = game;
        this.notifyObservers();
    }

    /**
     * Returns the game associated to this room
     * @author: Alessandro Nichelini
     * @return Game
     */
    public Game getGameAssociated() {
        return gameAssociated;
    }

    /**
     * Associates a game controller to this class
     * @author: Alessandro Nichelini
     * @param controller Controller to be associated
     */
    public void setGameControllerAssociated(GameController controller){
        this.gameControllerAssociated = controller;
        this.notifyObservers();
    }

    /**
     * Returns the game associated with this room
     * @author: Alessandro Nichelini
     * @return GamecController
     */
    public GameController getGameControllerAssociated() {
        return gameControllerAssociated;
    }

    /**
     * Returns the name of the room
     * @author: Alessandro Nichelini
     * @return String
     */
    public String getRoomName(){
        return this.roomName;
    }

    @Override
    public void addObserver(Observer o){
        this.observers.add(o);
    }

    @Override
    public void deleteObserver(Observer o){
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers(){
        if (this.hasChanged()){
            for (Observer observer :observers) {observer.update(this, null);}
        }
        this.clearChanged();
    }

    @Override
    public String toString(){
        return this.roomName;
    }

}
