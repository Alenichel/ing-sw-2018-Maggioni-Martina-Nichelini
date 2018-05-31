package it.polimi.se2018.message;

import it.polimi.se2018.utils.LoggerPriority;

public class ControllerCallbackMessage extends Message{

    private LoggerPriority priority;

    public ControllerCallbackMessage(String callback, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.stringMessage = callback;
        this.priority = priority;
    }

    public LoggerPriority getPriority() {
        return priority;
    }
}
