package it.polimi.se2018.view;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.VirtualViewNetworkBridge;

import java.util.Observable;
import java.util.Observer;

public class VirtualView extends View implements Observer {

    private VirtualViewNetworkBridge virtualClient;

    public VirtualView (VirtualViewNetworkBridge virtualClient, Player player){
        this.virtualClient = virtualClient;
        this.client = player;
        this.addObserver(ServerController.getInstance());
        Server.getInstance().addObserver(this);
    }

    public void mySetChanged(){
        this.setChanged();
    }

    //Da CONTROLLER a VIEW -> deve passare per la rete
    public void controllerCallback(Message callbackMessage){
        virtualClient.controllerCallback(callbackMessage);
    }

    //Da MODEL a VIEW -> deve passare per la rete
    public void update(Observable o, Object msg){
        virtualClient.update(o, msg);
    }

}
