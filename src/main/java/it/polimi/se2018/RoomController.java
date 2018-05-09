package it.polimi.se2018;

import it.polimi.se2018.message.Message;

import java.util.Observer;
import java.util.Observable;

public class RoomController implements Observer {

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

    public void setState(RoomControllerState state){
        this.state = state;
    }

    public RoomControllerState getState(){
        return this.state;
    }

    public void update(Observable observable, Object msg){
        switch(((Message)msg).getMessageType()){
            case "ConnectionMessage":
                this.connectPlayer(new Player("Ciao"));
                break;
        }
    }


}
