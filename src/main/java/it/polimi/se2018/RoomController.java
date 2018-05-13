package it.polimi.se2018;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.RoomConnectionMessage;

import java.util.Observer;
import java.util.Observable;

/**
 * Controller for class Room implemented trough a StatePattern.
 * Room controller is an observer of RoomView.
 */
public class RoomController implements Observer {

    public final Room room;
    private RoomControllerState state;

    /**
     *
     * @param state The initial state tou want for ypur class
     * @param room  The Room model class you the controller to be associated
     */
    public RoomController(RoomControllerState state, Room room){
        this.room = room;
        this.state = state;
    }

    /**
     * Launch the game depending on the current room controller state.
     */
    private void launchGame(){
        state.launchGame(this);
    }

    /**
     * Connect a player depending on the current room controller state.
     * @param player
     */
    private void connectPlayer( Player player){
        state.connectPlayer(this, player);
    }

    /**
     * Disconecct a player depending on the current room controller state.
     * @param player
     */
    private void disconnectPlayer(Player player){
        state.disconnectPlayer(this, player);
    }

    /**
     * Change the state of the room controller.
     * @param state The new state
     */
    public void setState(RoomControllerState state){
        this.state = state;
    }

    /**
     * State getter.
     * @return the current room controller state.
     */
    public RoomControllerState getState(){
        return this.state;
    }

    /**
     * Umpdate method to handle message coming from the view
     * @param observable
     * @param msg
     */
    public void update(Observable observable, Object msg){
        switch(((Message)msg).getMessageType()){
            case "ConnectionMessage":
                if (((RoomConnectionMessage)msg).isConnecting())
                    this.connectPlayer(((RoomConnectionMessage)msg).getRequester());
                else this.disconnectPlayer(((RoomConnectionMessage)msg).getRequester());
                break;
        }
    }


}
