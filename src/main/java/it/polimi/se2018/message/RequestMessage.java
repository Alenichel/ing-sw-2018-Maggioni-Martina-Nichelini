package it.polimi.se2018.message;

public class RequestMessage extends Message{

    private String request;

    public RequestMessage(String request){
        this.request = request;
        this.messageType = "RequestMessage";
    }

    public String getRequest() {
        return request;
    }
}