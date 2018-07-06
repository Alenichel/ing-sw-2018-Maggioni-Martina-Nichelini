package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

/**
 * Class for handshake connection message
 */
public class HandshakeConnectionMessage extends Message {

    private String username;
    private String encodedPassword;
    private Player player;

    /**
     * Class constructor
     * @param username player's username
     * @param password player's password
     */
    public HandshakeConnectionMessage(String username, String password){
        this.messageType = "HandshakeConnectionMessage";
        this.username = username;
        this.encodedPassword=password;
    }

    /**
     * Class constructor
     * @param player player connected
     */
    public HandshakeConnectionMessage(Player player){
        this.messageType = "HandshakeConnectionMessage";
        this.player = player;
    }

    /**
     * player getter
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * username getter
     * @return username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * encoded password getter
     * @return encoded password
     */
    public String getEncodedPassword() {
        return encodedPassword;
    }
}