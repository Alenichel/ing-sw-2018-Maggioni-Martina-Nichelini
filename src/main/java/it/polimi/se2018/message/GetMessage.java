package it.polimi.se2018.message;

public class GetMessage extends Message{

    private String toGet;

    public GetMessage(String toGet){
        this.toGet = toGet;
        this.messageType = "GetMessage";
    }

    public String getToGet() {
        return toGet;
    }
}
