package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.CallbackMessageSubject;
import it.polimi.se2018.enumeration.LoggerPriority;

public class ControllerCallbackMessage extends Message{

    private LoggerPriority priority;
    private CallbackMessageSubject callbackMessageSubject;

    public ControllerCallbackMessage(CallbackMessageSubject callback, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.callbackMessageSubject = callback;
        this.priority = priority;
    }

    public ControllerCallbackMessage(CallbackMessageSubject callback, String stringMessage, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.callbackMessageSubject = callback;
        this.stringMessage = stringMessage;
        this.priority = priority;
    }

    public ControllerCallbackMessage(String callback, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.stringMessage = callback;
        this.priority = priority;
    }

    public LoggerPriority getPriority() {
        return priority;
    }

    public CallbackMessageSubject getCallbackMessageSubject() {
        return callbackMessageSubject;
    }
}