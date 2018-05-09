package it.polimi.se2018;

import it.polimi.se2018.message.Message;

public class RoomController {

    public final Room room;
    private RoomControllerState state;

    public RoomController(RoomControllerState state, Room room){
        this.room = room;
        this.state = state;
    }

    private void launchGame(){
        state.launchGame(this);
    }

    private void connectPlayer( Player player){
        state.connectPlayer(this, player);
    }

    private void disconnectPlayer(Player player){
        state.disconnectPlayer(this, player);
    }

    private void setState(RoomControllerState state){
        this.state = state;
    }

    private RoomControllerState getState(){
        return this.state;
    }

    private Room getAssociatedRoom(){
        return this.room;
    }

    public void update(Message msg){
        switch(msg.getMessageType()){
            case "ConnectionMessage":
                this.connectPlayer(new Player("Ciao"));
                break;
        }
    }
}
