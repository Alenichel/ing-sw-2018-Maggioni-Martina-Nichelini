package it.polimi.se2018;

public class RoomController {

    public final Room room;
    private RoomControllerState state;

    public RoomController(RoomControllerState state, Room room){
        this.room = room;
        this.state = state;
    }

    public void launchGame(){
        state.launchGame(this);
    }

    public void connectPlayer( Player player){
        state.connectPlayer(this, player);
    }

    public void disconnectPlayer(Player player){
        state.disconnectPlayer(this, player);
    }

    public void setState(RoomControllerState state){
        this.state = state;
    }

    public RoomControllerState getState(){
        return this.state;
    }

    public Room getAssociatedRoom(){
        return this.room;
    }
}
