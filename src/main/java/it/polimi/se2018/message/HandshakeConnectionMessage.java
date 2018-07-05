package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

public class HandshakeConnectionMessage extends Message {

    private String username;
    private String encodedPassword;
    private Player player;

    public HandshakeConnectionMessage(String username, String password){
        this.messageType = "HandshakeConnectionMessage";
        this.username = username;
        this.encodedPassword=password;
    }

    public HandshakeConnectionMessage(Player player){
        this.messageType = "HandshakeConnectionMessage";
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }
}