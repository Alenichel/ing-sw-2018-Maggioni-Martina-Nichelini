package it.polimi.se2018.message;

import it.polimi.se2018.model.Player;

/**
 * Class for selection message
 */
public class SelectionMessage extends Message{

    private Object chosenItem;
    private Player fromWhom;
    private String selected;

    /**
     * Class constructor
     */
    public SelectionMessage(Object item, Player player){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  player;
    }

    /**
     * Class constructor
     */
    public SelectionMessage(Object item, Player player, String selected){
        this.chosenItem = item;
        this.messageType = "SelectionMessage";
        this.fromWhom =  player;
        this.selected = selected;
    }

    /**
     * selected getter
     * @return selected
     */
    public String getSelected() {
        return selected;
    }

    /**
     * player getter
     * @return player
     */
    public Player getPlayer(){
        return this.fromWhom;
    }

    /**
     * chosen item getter
     * @return chosen item
     */
    public Object getChosenItem(){
        return chosenItem;
    }
}