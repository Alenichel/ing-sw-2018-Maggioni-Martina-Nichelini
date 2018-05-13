package it.polimi.se2018.message;

import it.polimi.se2018.Player;
import it.polimi.se2018.Room;

public class RoomConnectionMessage extends Message{

    private Player requester;

    public RoomConnectionMessage(Player requester){
        this.messageType = "ConnectionRequest";
        this.requester = requester;
    }

    public Player getRequester() {
        return requester;
    }
}
