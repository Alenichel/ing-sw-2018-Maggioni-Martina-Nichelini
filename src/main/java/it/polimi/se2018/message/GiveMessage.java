package it.polimi.se2018.message;

/**
 * Class for give message
 */
public class GiveMessage extends Message {

    private Object givenObject;
    private String giving;

    /**
     * Class constructor
     */
    public GiveMessage(String giving, Object givenObject){
        this.givenObject = givenObject;
        this.messageType = "GiveMessage";
        this.giving = giving;
    }

    /**
     * given object getter
     * @return given object
     */
    public Object getGivenObject() {
        return givenObject;
    }

    /**
     * giving getter
     * @return giving
     */
    public String getGiving() {
        return giving;
    }

}