package it.polimi.se2018;

import it.polimi.se2018.message.GiveMessage;

import java.util.Observable;

abstract class View extends Observable {

    protected Player client;

    abstract void requestCallback(GiveMessage giveMessage);

    public Player getClient() {
        return client;
    }
}
