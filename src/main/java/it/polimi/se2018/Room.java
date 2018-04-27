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

    public Room(String name, Player admin, Boolean singlePlayer){
        this.roomId = this.hashCode();
        this.roomName = name;
        this.singlePlayerMode = singlePlayer;
        this.started = false;

        this.maxNOfPlayers = singlePlayerMode ? 1 : 4;
        this.numberOfConnectedPlayer = 1;
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

    public boolean getStarted(){
        return started;
    }

    public void setStarted(boolean status){
        this.started = status;
    }

    public String getRoomName(){
        return this.roomName;
    }

    public int getRoomId(){
        return this.roomId;
    }
}
