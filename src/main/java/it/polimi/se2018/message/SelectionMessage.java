package it.polimi.se2018.message;

import it.polimi.se2018.Player;

public class SelectionMessage extends Message{

    private Object chosenItem;
    private int fromWhom;

    public SelectionMessage(Object item, int playerNumber){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  playerNumber;
    }

    public int getPlayerNumber(){
        return this.fromWhom;
    }

    public Object getChosenItem(){
        return chosenItem;
    }
}