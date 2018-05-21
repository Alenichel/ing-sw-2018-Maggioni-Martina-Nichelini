package it.polimi.se2018.message;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;

/**
 * Message class for connecting/disconneting action
 */
public class ConnectionMessage extends Message{

    private Player requester;
    private boolean isConnecting;
    private Game target;

    /**
     * Constructor for class
     * @param requester The player is connecting/disconnetting
     * @param isConnecting True if the player is connecting, False if the player is disconneting.
     */
    public ConnectionMessage(Player requester, boolean isConnecting){
        this.messageType = "ConnectionMessage";
        this.requester = requester;
        this.isConnecting = isConnecting;
    }

    public Player getRequester() {
        return requester;
    }

    public Game getTarget() {
        return target;
    }

    public boolean isConnecting() {
        return isConnecting;
    }
}
