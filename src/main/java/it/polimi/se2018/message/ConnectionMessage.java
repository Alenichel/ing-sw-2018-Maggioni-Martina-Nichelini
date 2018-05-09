package it.polimi.se2018.message;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConnectionMessage extends Message {

    private String username;
    private byte[] encodedPassword;

    public ConnectionMessage(String username, String password){
        this.messageType = "ConnectionMessage";
        this.username = username;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {;}
    }

    public String getUsername(){
        return this.username;
    }
}
