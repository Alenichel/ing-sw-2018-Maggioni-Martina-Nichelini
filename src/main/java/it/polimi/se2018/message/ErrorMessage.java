package it.polimi.se2018.message;

public class ErrorMessage extends Message {

    public ErrorMessage(String error){
        this.stringMessage = error;
    }

    @Override
    public String toString(){
        return "[*] ERROR: " + this.getStringMessage();
    }
}
