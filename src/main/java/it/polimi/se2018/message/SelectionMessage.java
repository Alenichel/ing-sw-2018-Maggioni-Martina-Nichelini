package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

public class SelectionMessage extends Message{

    private Object chosenItem;
    private Player fromWhom;
    private String selected;

    public SelectionMessage(Object item, Player player){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  player;
    }

    //aggiunto per mandare da cliview al server il messaggio di scelta della patternCard,
    public SelectionMessage(Object item, Player player, String selected){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  player;
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }

    public Player getPlayer(){
        return this.fromWhom;
    }

    public Object getChosenItem(){
        return chosenItem;
    }
}