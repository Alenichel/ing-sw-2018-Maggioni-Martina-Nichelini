package it.polimi.se2018.message;

public class SelectionMessage extends Message{

    private Object chosenItem;
    private int fromWhom;
    private String selected;

    public SelectionMessage(Object item, int playerNumber){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  playerNumber;
    }

    //aggiunto per mandare da cliview al server il messaggio di scelta della patternCard,
    public SelectionMessage(Object item, int playerNumber, String selected){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  playerNumber;
        this.selected = selected;
    }

    public int getPlayerNumber(){
        return this.fromWhom;
    }

    public Object getChosenItem(){
        return chosenItem;
    }
}