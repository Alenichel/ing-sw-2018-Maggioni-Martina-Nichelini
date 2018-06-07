package it.polimi.se2018.network;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SocketUpdateContainer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.utils.Security;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;
import javafx.util.Pair;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;


public class RMIServerImplementation extends UnicastRemoteObject implements
		ServerInterface {

    //private Player player;
   // private  VirtualView associatedVirtualView;

	//private ArrayList<ClientInterface> clients = new ArrayList<>();

	private HashMap clients = new HashMap<String, Pair<ClientInterface, View>>();

	protected RMIServerImplementation() throws RemoteException {
		super(0);		
	}

	@Override
	public void addClient(ClientInterface client) throws RemoteException {
        Player player;
	    try {
            player = Security.authenticateUser(client.getInsertedNickname(), client.getInsertedPassword());
            client.setPlayer(player);
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "(RMI) RMIClient " + client.getInsertedNickname() + " connected!");
        } catch (AuthenticationErrorException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, "Login error: connection aborted");
            return;
        }

        VirtualView vv = new VirtualView(this, player);
        Pair clientView = new Pair<>(client, vv);
        clients.put(client.getInsertedNickname(), clientView);
        vv.mySetChanged();
        vv.notifyObservers(new ConnectionMessage(player, true));
        //clients.add(client);
        //this.associatedVirtualView = new VirtualView(this, this.player);
    }

	@Override
    public void controllerCallback(Message callbackMessage){
	    try {
	        Pair pair = (Pair)clients.get(callbackMessage.getSignedBy());
            ClientInterface client = (ClientInterface) pair.getKey();
            client.notify(callbackMessage);
	        //clients.get(0).notify(callbackMessage); (client, vv);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object msg){
        try {
            SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
            Pair pair = ((Pair)clients.get(((Message)msg).getSignedBy()));
            ClientInterface client = (ClientInterface) pair.getKey();
            client.notify(suc);
        } catch (Exception e) {
        e.printStackTrace();
        }
	}

	public void receiver(Observable o, Object msg) {
        String key = ((Message)msg).getSignedBy();
        Pair pair = (Pair) clients.get(key);
        VirtualView view = (VirtualView) pair.getValue();
        view.mySetChanged();
        view.notifyObservers(/*packet.getObservable(), */msg);
    }
}
