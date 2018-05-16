package it.polimi.se2018.controller;

import it.polimi.se2018.view.*;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.CreationalMessage;
import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Room;
import it.polimi.se2018.model.Server;

import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer{
    private final static ServerController instance = new ServerController(Server.getInstance());
    private Server server;

    private RoomController roomController = RoomController.getInstance();

    public static ServerController getInstance() {
        return instance;
    }

    private ServerController(Server server) {
        this.server = server;
    }

    private void connectPlayer (Player player) {
        server.addPlayer(player);
        player.setOnline(true);
    }

    private void disconnectPlayer (Player player) {
        server.removePlayer(player);
        player.setOnline(false);
    }

    private void createRoom(String roomName, String adminName) {
        Room r = new Room(roomName, admin, false);
        server.addRoom(r);
        admin.setRoom(r, true);
    }

    private void deleteRoom(Room room) {
        server.removeRoom(room);
    }


    public void update (Observable observable, Object message){
        switch(((Message)message).getMessageType()){

            case "ConnectionMessage":
                if (((ConnectionMessage)message).isConnecting()){
                    if ( ((ConnectionMessage)message).getTarget() == null  )
                        this.connectPlayer(((ConnectionMessage)message).getRequester());
                }
                else {
                    if ( ((ConnectionMessage)message).getTarget() == null  )
                        this.disconnectPlayer(((ConnectionMessage)message).getRequester());
                }
                break;

            case "CreationalMessage":
                CreationalMessage msg = ((CreationalMessage)message);
                if (msg.getWhatToCreate() == "Room") {
                    this.createRoom( msg.getName(), ((View)observable).getClient()  );
                }
                break;

            case "RequestMessage":
                ((View)observable).requestCallback(new GiveMessage("ActiveRooms", server.getActiveGames()));
                break;

            default:
                break;
        }
    }

}