package it.polimi.se2018.view;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class View extends Observable implements Serializable {

    protected Player client;

    public abstract void controllerCallback(Message msg);

    public abstract void update(Observable observable, Object message);

    public Player getClient() {
        return client;
    }
}
