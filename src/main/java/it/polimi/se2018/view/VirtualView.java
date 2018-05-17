package it.polimi.se2018.view;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.network.VirtualClient;

import java.util.Observable;
import java.util.Observer;

public class VirtualView extends View implements Observer {

    private VirtualClient virtualClient;

    public VirtualView (VirtualClient virtualClient){
        this.virtualClient = virtualClient;
        this.addObserver(ServerController.getInstance());
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
