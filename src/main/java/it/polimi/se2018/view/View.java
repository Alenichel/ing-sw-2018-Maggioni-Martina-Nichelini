package it.polimi.se2018.view;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.MoveDiceMessage;
import it.polimi.se2018.model.Player;

import java.io.Serializable;
import java.util.Observable;

public abstract class View extends Observable implements Serializable {

    protected Player client;

    public abstract void controllerCallback(Message msg);

    public abstract void update(Observable observable, Object message);

    public Player getClient() {
        return client;
    }

    public void setPlayer(Player player) {
        this.client = player;
    }

    public void placeDice(int n, int endingX, int endingY) {
        MoveDiceMessage mdm = new MoveDiceMessage(n, endingX - 1, endingY - 1);
        this.setChanged();
        this.notifyObservers(mdm);
    }

}
