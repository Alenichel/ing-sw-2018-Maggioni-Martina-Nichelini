package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String messageType;
    protected String stringMessage;
    protected String signedBy;

    public String getMessageType(){
        return this.messageType;
    }

    public String getStringMessage() {
        return stringMessage;
    }

    public void setStringMessage(String stringMessage) {
        this.stringMessage = stringMessage;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    @Override
    public String toString(){
        return messageType + ": " +  stringMessage;
    }
}
