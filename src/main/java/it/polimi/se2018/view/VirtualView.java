package it.polimi.se2018.view;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.ServerInterface;

import java.util.Observable;
import java.util.Observer;

public class VirtualView extends View implements Observer {

    private ServerInterface virtualClient = null;
    private View concreteView = null;

    public VirtualView(ServerInterface virtualClient, Player player) {
        this.virtualClient = virtualClient;
        this.client = player;
        this.addObserver(ServerController.getInstance());
        Server.getInstance().addObserver(this);
    }

    public VirtualView(View concreteView, Player player){
        this.concreteView = concreteView;
        this.client = player;
        this.addObserver(ServerController.getInstance());
        Server.getInstance().addObserver(this);
    }


    public void controllerCallback(Message callbackMessage) {
        try {
            callbackMessage.setSignedBy(this.client.getNickname());
            if (virtualClient != null) virtualClient.controllerCallback(callbackMessage);
            else if (concreteView != null) concreteView.controllerCallback(callbackMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Observable o, Object msg) {
        try {
            ((Message)msg).setSignedBy(this.client.getNickname());
            if (virtualClient != null) virtualClient.update(o, msg);
            else if (concreteView != null) concreteView.update(o, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
