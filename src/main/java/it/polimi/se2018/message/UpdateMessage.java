package it.polimi.se2018.message;

public class UpdateMessage extends Message {

    private String whatToUpdate;


    public UpdateMessage(String whatToUpdate){
        this.whatToUpdate = whatToUpdate;
        this.messageType = "UpdateMessage";
    }


    public String getWhatToUpdate() {
        return whatToUpdate;
    }

    @Override
    public String toString(){
        return this.stringMessage;
    }
}
