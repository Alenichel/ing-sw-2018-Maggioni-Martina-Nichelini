package it.polimi.se2018.message;

/**
 * Class for request message
 */
public class RequestMessage extends Message{

    private String request;

    /**
     * Class constructor
     */
    public RequestMessage(String request){
        this.request = request;
        this.messageType = "RequestMessage";
    }

    /**
     * request getter
     * @return
     */
    public String getRequest() {
        return request;
    }
}