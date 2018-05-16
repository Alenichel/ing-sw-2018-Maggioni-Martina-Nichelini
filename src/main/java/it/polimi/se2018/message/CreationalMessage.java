package it.polimi.se2018.message;

public class CreationalMessage extends Message {

    private String whatToCreate;
    private String name;

    public CreationalMessage(String whatToCreate, String name){
        this.whatToCreate = whatToCreate;
        this.messageType = "CreationalMessage";
        this.name = name;
    }

    public String getWhatToCreate() {
        return whatToCreate;
    }

    public String getName(){
        return this.name;
    }
}
