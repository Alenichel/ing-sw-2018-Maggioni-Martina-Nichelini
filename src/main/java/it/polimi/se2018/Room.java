package it.polimi.se2018;
import java.util.ArrayList;
import java.util.List;

public class Room {
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
    }

    public void removePlayer(Player player){
        listOfConnectedPlayer.remove(player);
        numberOfConnectedPlayer = numberOfConnectedPlayer - 1;
    }

    public List<Player> getListOfConnectedPlayer() {
        return listOfConnectedPlayer;
    }

    public void addDisconnectedClient(Player player){
        disconnectedClients.add(player);
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
    }

    public void setGameAssociated(Game game){
        this.gameAssociated = game;
    }

    public Game getGameAssociated() {
        return gameAssociated;
    }

    public void setGameControllerAssociated(GameController controller){
        this.gameControllerAssociated = controller;
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
}
