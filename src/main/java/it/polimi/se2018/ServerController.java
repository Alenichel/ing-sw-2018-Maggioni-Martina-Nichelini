package it.polimi.se2018;

import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.message.Message;

import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer{
    private final static ServerController instance = new ServerController(Server.getInstance());
    private Server server;

    private RoomController roomController = new RoomController();

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

    private void createRoom(String gameName, Player admin) {
        server.addRoom(gameName, admin);
    }

    private void deleteRoom(Room room) {
        server.removeRoom(room);
    }


    public void update (Observable observable, Object msg){
        switch(((Message)msg).getMessageType()){

            case "ConnectionMessage":
                if (((ConnectionMessage)msg).isConnecting()){
                    if ( ((ConnectionMessage)msg).getTarget() == null  )
                        this.connectPlayer(((ConnectionMessage)msg).getRequester());
                }
                else {
                    if ( ((ConnectionMessage)msg).getTarget() == null  )
                        this.disconnectPlayer(((ConnectionMessage)msg).getRequester());
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