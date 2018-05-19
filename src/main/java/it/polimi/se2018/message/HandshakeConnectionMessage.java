package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HandshakeConnectionMessage extends Message {

    private String username;
    private byte[] encodedPassword;
    private Player player;

    public HandshakeConnectionMessage(String username, String password){
        this.messageType = "HandshakeConnectionMessage";
        this.username = username;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {;}
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

    public byte[] getEncodedPassword() {
        return encodedPassword;
    }
}
