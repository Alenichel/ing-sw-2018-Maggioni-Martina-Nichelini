package it.polimi.se2018.message;

public class ControllerCallbackMessage extends Message{

    public ControllerCallbackMessage(String callback){
        this.messageType = "ControllerCallbackMessage";
        this.stringMessage = callback;
    }
}
