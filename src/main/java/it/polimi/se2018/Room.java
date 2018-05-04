package it.polimi.se2018;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Room extends java.util.Observable{

    private List<Observer> observers = new ArrayList<>();

    private int roomId;
    private String roomName;
    private boolean singlePlayerMode;
    private boolean started;
    private int maxNOfPlayers;
    private int numberOfConnectedPlayer;
    private ArrayList <Player> listOfConnectedPlayer = new ArrayList<>();
    private Game gameAssociated;
    private GameController gameControllerAssociated;
    private ArrayList <Player> disconnectedClients = new ArrayList<>();

    public Room(String name, Player admin, Boolean singlePlayer){
        this.roomId = this.hashCode();
        this.roomName = name;
        this.singlePlayerMode = singlePlayer;
        this.started = false;

        this.maxNOfPlayers = singlePlayerMode ? 1 : 4;
        this.numberOfConnectedPlayer = 1;
        this.gameAssociated = null; //no game associated at room initialization
        this.gameControllerAssociated = null; //no gameController associated at room initialization
        listOfConnectedPlayer.add(admin);
    }

    // you can add a player only if there is an empty seat in the room
    public void addPlayer(Player player){
        if (numberOfConnectedPlayer + 1 > maxNOfPlayers) throw new IndexOutOfBoundsException("The room is full");

        listOfConnectedPlayer.add(player);
        numberOfConnectedPlayer = numberOfConnectedPlayer + 1;
        this.notifyObservers();
    }

    public void removePlayer(Player player){
        listOfConnectedPlayer.remove(player);
        numberOfConnectedPlayer = numberOfConnectedPlayer - 1;
        this.notifyObservers();
    }

    public List<Player> getListOfConnectedPlayers() {
        return listOfConnectedPlayer;
    }

    public void addDisconnectedClient(Player player){
        disconnectedClients.add(player);
        this.notifyObservers();
    }

    public List<Player> getListOfDisconnectedClients(){
        return disconnectedClients;
    }

    public boolean isADisconnectedClient(Player player){
        if (this.disconnectedClients.contains(player)) return true;
        else return false;
    }

    public boolean getStarted(){
        return started;
    }

    public void setStarted(boolean status){
        this.started = status;
        this.notifyObservers();
    }

    public void setGameAssociated(Game game){
        this.gameAssociated = game;
        this.notifyObservers();
    }

    public Game getGameAssociated() {
        return gameAssociated;
    }

    public void setGameControllerAssociated(GameController controller){
        this.gameControllerAssociated = controller;
        this.notifyObservers();
    }

    public GameController getGameControllerAssociated() {
        return gameControllerAssociated;
    }

    public String getRoomName(){
        return this.roomName;
    }

    public int getRoomId(){
        return this.roomId;
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

}
