package it.polimi.se2018.view;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.MoveDiceMessage;
import it.polimi.se2018.model.Player;

import java.io.Serializable;
import java.util.Observable;

/**
 * Abstract class for view
 */
public abstract class View extends Observable implements Serializable {

    protected Player client;

    /**
     * Controller callback message
     * @param msg message
     */
    public abstract void controllerCallback(Message msg);

    /**
     * Update method
     */
    public abstract void update(Observable observable, Object message);

    /**
     * client getter
     * @return client
     */
    public Player getClient() {
        return client;
    }

    /**
     * set changed method
     */
    public void mySetChanged() {
        this.setChanged();
    }

    /**
     * client setter
     * @param player client
     */
    public void setPlayer(Player player) {
        this.client = player;
    }

    /**
     * place die method
     * @param n number of the die
     * @param endingX ending cell x
     * @param endingY ending cell y
     */
    public void placeDice(int n, int endingX, int endingY) {
        MoveDiceMessage mdm = new MoveDiceMessage(n, endingX - 1, endingY - 1);
        this.setChanged();
        this.notifyObservers(mdm);
    }

}
