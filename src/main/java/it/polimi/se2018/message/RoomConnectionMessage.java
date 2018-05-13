package it.polimi.se2018.message;

import it.polimi.se2018.Player;
import it.polimi.se2018.Room;

public class RoomConnectionMessage extends Message{

    private Player requester;
    private boolean isConnecting;

    public RoomConnectionMessage(Player requester, boolean isConnecting){
        this.messageType = "ConnectionMessage";
        this.requester = requester;
        this.isConnecting = isConnecting;
    }

    public Player getRequester() {
        return requester;
    }

    public boolean isConnecting() {
        return isConnecting;
    }
}
