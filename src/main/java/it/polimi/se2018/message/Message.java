package it.polimi.se2018.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String messageType;
    protected String stringMessage;

    public String getMessageType(){
        return this.messageType;
    }

    public String getStringMessage() {
        return stringMessage;
    }

    public void setStringMessage(String stringMessage) {
        this.stringMessage = stringMessage;
    }

    @Override
    public String toString(){
        return messageType + ": " +  stringMessage;
    }
}
