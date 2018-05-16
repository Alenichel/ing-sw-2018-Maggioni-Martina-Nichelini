package it.polimi.se2018.message;

public class GiveMessage extends Message {

    private Object givenObject;
    private String giving;

    public GiveMessage(String giving, Object givenObject){
        this.givenObject = givenObject;
        this.messageType = "GiveMessage";
        this.giving = giving;
    }

    public Object getGivenObject() {
        return givenObject;
    }

    public String getGiving() {
        return giving;
    }

}
