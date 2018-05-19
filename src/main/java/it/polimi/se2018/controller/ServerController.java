package it.polimi.se2018.controller;

import it.polimi.se2018.message.*;
import it.polimi.se2018.view.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Room;
import it.polimi.se2018.model.Server;

import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer{
    private static ServerController instance = null;
    private Server server = Server.getInstance();
    private RoomController roomController = RoomController.getInstance();

    private Player getPlayerFromNick(String nickname){
        for (Player p : server.getOnlinePlayers()){
            if (p.getNickname().equals(nickname))
                return p;
        }
        return null;
    }

    public static ServerController getInstance(){
        if(instance == null){
            instance = new ServerController();
        }
        return instance;
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
        Player admin = this.getPlayerFromNick(adminName);
        Room room = new Room(roomName,admin, false);
        server.addRoom(room);
        admin.setRoom(room, true);
    }

    private void deleteRoom(Room room) {
        server.removeRoom(room);
    }

    public void update (Observable observable, Object message){

        System.out.println(((Message)message).getMessageType());
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
                CreationalMessage cMsg = ((CreationalMessage)message);
                if (cMsg.getWhatToCreate().equals("Room")) {
                    this.createRoom( cMsg.getName(), ((View)observable).getClient().getNickname()  );
                }
                break;

            case "RequestMessage":
                RequestMessage rMsg = ((RequestMessage)message);

                if (rMsg.getRequest().equals("SubscribePlayer")) {
                    this.getPlayerFromNick(((View) observable).getClient().getNickname()).addObserver((Observer) observable);
                    System.out.println("iscrittoAlPlayer");
                }

                else if (rMsg.getRequest().equals("SubscribeServer")) {
                    server.addObserver((Observer)observable);
                    System.out.println("iscrittoAlServer");
                }

                else if (rMsg.getRequest().equals("UnsubscribeServer")) {
                    server.deleteObserver((Observer)observable);
                    System.out.println("disiscrittoAlServer");
                }

                else if (rMsg.getRequest().equals("ActiveRooms"))
                    ((View)observable).controllerCallback(new GiveMessage("ActiveRooms", server.getActiveGames()));
                break;

            default:
                break;
        }
    }

}