package it.polimi.se2018.view;

import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.model.Player;

import java.util.Observable;

public abstract class View extends Observable {

    protected Player client;

    public abstract void requestCallback(GiveMessage giveMessage);

    public Player getClient() {
        return client;
    }
}
