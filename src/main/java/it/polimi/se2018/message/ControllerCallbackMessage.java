package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.CallbackMessageSubject;
import it.polimi.se2018.enumeration.LoggerPriority;

/**
 * Class for controller callback's message
 */
public class ControllerCallbackMessage extends Message{

    private LoggerPriority priority;
    private CallbackMessageSubject callbackMessageSubject;

    public ControllerCallbackMessage(CallbackMessageSubject callback, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.callbackMessageSubject = callback;
        this.priority = priority;
    }

    /**
     * Class constructor
     */
    public ControllerCallbackMessage(CallbackMessageSubject callback, String stringMessage, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.callbackMessageSubject = callback;
        this.stringMessage = stringMessage;
        this.priority = priority;
    }

    /**
     * Class constructor
     */
    public ControllerCallbackMessage(String callback, LoggerPriority priority){
        this.messageType = "ControllerCallbackMessage";
        this.stringMessage = callback;
        this.priority = priority;
    }

    /**
     * priority getter
     * @return priority
     */
    public LoggerPriority getPriority() {
        return priority;
    }

    /**
     * subject getter
     * @return subject
     */
    public CallbackMessageSubject getCallbackMessageSubject() {
        return callbackMessageSubject;
    }
}