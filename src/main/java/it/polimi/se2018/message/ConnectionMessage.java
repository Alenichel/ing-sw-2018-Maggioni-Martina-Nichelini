package it.polimi.se2018.message;

import it.polimi.se2018.Player;
import it.polimi.se2018.Room;

/**
 * Message class for connecting/disconneting action
 */
public class ConnectionMessage extends Message{

    private Player requester;
    private boolean isConnecting;

    /**
     * Constructor for class
     * @param requester The player is connecting/disconnetting
     * @param isConnecting True if the player is connecting, False if the player is disconneting.
     */
    public ConnectionMessage(Player requester, boolean isConnecting){
        this.messageType = "HandshakeConnectionMessage";
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
