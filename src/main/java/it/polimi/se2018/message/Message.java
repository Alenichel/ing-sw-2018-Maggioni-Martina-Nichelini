package it.polimi.se2018.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String clientID;
    protected String messageType;
    protected String stringMessage;

    public String getMessageType(){
        return this.messageType;
    }
}
