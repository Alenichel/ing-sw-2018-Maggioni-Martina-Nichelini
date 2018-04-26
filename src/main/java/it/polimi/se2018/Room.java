package it.polimi.se2018;
import java.util.ArrayList;

public class Room {
    private int gameId;
    private String gameName;
    private boolean singlePlayerMode;
    private boolean started;
    private int numberOfPlayers;
    private int numberOfConnectedPlayer;
    private ArrayList <Player> listOfConnectedPlayer;
    private static final int  numberOfRound = 10;
    private int currentRound;

    public Room(String name, Player admin, Boolean singlePlayer){
        this.gameId = this.hashCode();
        this.gameName = name;
        this.singlePlayerMode = singlePlayer;
        this.started = false;

        this.numberOfPlayers = singlePlayer ? 1 : 4;
        this.numberOfConnectedPlayer = 1;
        listOfConnectedPlayer.add(admin);
    }

    public void gameSetter(){
        //for now empty
    }

    public void connectPlayer(Player player){
        //for now empty
    }

    public void disconnectPlayer(Player player){
        //for now empty
    }

}
