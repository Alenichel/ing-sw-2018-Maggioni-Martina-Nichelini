package it.polimi.se2018.message;

import java.io.Serializable;

/**
 * Abstract class for message
 */
public abstract class Message implements Serializable {
    protected String messageType;
    protected String stringMessage;
    protected String signedBy;

    /**
     * message type getter
     * @return message type
     */
    public String getMessageType(){
        return this.messageType;
    }

    /**
     * string message getter
     * @return string message
     */
    public String getStringMessage() {
        return stringMessage;
    }

    /**
     * string message setter
     */
    public void setStringMessage(String stringMessage) {
        this.stringMessage = stringMessage;
    }

    /**
     * signed by getter
     * @return signed by
     */
    public String getSignedBy() {
        return signedBy;
    }

    /**
     * signed by setter
     */
    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    /**
     * to string method
     */
    @Override
    public String toString(){
        return messageType + ": " +  stringMessage;
    }
}