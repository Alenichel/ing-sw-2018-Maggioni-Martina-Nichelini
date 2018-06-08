package it.polimi.se2018.view;

import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;

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

    public void setPlayer(Player player) {
        this.client = player;
    }
}
