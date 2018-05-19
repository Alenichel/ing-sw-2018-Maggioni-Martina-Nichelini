package it.polimi.se2018.controller;

import it.polimi.se2018.view.*;
import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.RequestMessage;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Room;


import java.util.Observer;
import java.util.Observable;

/**
 * Controller for class Room implemented trough a StatePattern.
 * Room controller is an observer of RoomView.
 */
public class RoomController implements Observer {

    private static RoomController instance = null;

    public static RoomController getInstance(){
        if(instance == null){
            instance = new RoomController();
        }
        return instance;
    }

    /**
     * Launch the game depending on the current room controller state.
     */
    private void launchGame(Room room){
        //room.launchGame(this);
    }

    /**
     * Connect a player depending on the current room controller state.
     * @param player
     */
    private void connectPlayer(Room room, Player player){
        room.addPlayer(player);
        player.setRoom(room,true);
    }

    /**
     * Disconecct a player depending on the current room controller state.
     * @param player
     */
    private void disconnectPlayer(Room room, Player player){
        room.removePlayer(player);
        player.setRoom(room,false);
    }

    /**
     * Umpdate method to handle message coming from the view
     * @param observable
     * @param msg
     */
    public void update(Observable observable, Object msg){
        switch(((Message)msg).getMessageType()){
            case "ConnectionMessage":
                if (((ConnectionMessage)msg).isConnecting())
                    this.connectPlayer(((ConnectionMessage)msg).getTarget() ,((ConnectionMessage)msg).getRequester());
                else this.disconnectPlayer(((ConnectionMessage)msg).getTarget() ,((ConnectionMessage)msg).getRequester());
                break;

            case "RequestMessage":
                RequestMessage rMsg = (RequestMessage)msg;
                if (rMsg.getRequest().equals("ConnectedPlayers")){
                    ((View)observable).controllerCallback(new GiveMessage("Players", ((View)observable).getClient().getRoom().getListOfConnectedPlayers()));
                }


            default: break;
        }
    }



}
