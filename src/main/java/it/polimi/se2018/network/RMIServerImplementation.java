package it.polimi.se2018.network;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SocketUpdateContainer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.utils.Security;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;
import javafx.util.Pair;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * RMI Server implementation
 */
public class RMIServerImplementation extends UnicastRemoteObject implements
		ServerInterface {

	private HashMap clients = new HashMap<String, Pair<ClientInterface, View>>();
	protected RMIServerImplementation() throws RemoteException {
		super(0);		
	}

    /**
     * This method adds a client
     * @param client client to be added
     */
	@Override
	public void addClient(ClientInterface client) throws RemoteException {
        Player player;
	    try {
            player = Security.authenticateUser(client.getInsertedNickname(), client.getInsertedPassword());
            client.setPlayer(player);
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "(RMI) RMIClient \"" + client.getInsertedNickname() + "\" connected!");
        } catch (AuthenticationErrorException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, "Login error: connection aborted");
            throw new RemoteException();
        }

        VirtualView vv = new VirtualView(this, player);
	    player.setVv(vv);
        Pair clientView = new Pair<>(client, vv);
        clients.put(client.getInsertedNickname(), clientView);
        vv.mySetChanged();
        vv.notifyObservers(new ConnectionMessage(player, true));
    }

    /**
     * controller callback
     */
	@Override
    public void controllerCallback(Message callbackMessage){
	    try {
	        Pair pair = (Pair)clients.get(callbackMessage.getSignedBy());
            ClientInterface client = (ClientInterface) pair.getKey();
            client.notify(callbackMessage);
        } catch ( Exception e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * update method
     */
    @Override
    public void update(Observable o, Object msg){
        try {
            SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
            Pair pair = ((Pair)clients.get(((Message)msg).getSignedBy()));
            ClientInterface client = (ClientInterface) pair.getKey();
            client.notify(suc);
        } catch (ConnectException e) {
            String pName = ((Message)msg).getSignedBy();
            Pair pair = (Pair)clients.get(pName);
            VirtualView vv = (VirtualView) pair.getValue();
            Player p = vv.getClient();
            vv.mySetChanged();
            vv.notifyObservers(new ConnectionMessage(p, false));
            clients.remove(pName);
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, "Player: " + pName +" || The RMI connection seems to have died");
        } catch (RemoteException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
        }
	}

    /**
     * receiver method
     */
	@Override
	public void receiver(Observable o, Object msg) {
        String key = ((Message)msg).getSignedBy();
        Pair pair = (Pair) clients.get(key);
        VirtualView view = (VirtualView) pair.getValue();
        view.mySetChanged();
        view.notifyObservers(/*packet.getObservable(), */msg);
    }

    /**
     * pong method
     */
    @Override
    public void pong () {
	    Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.DEBUG, "Received PING. PONGED");
    }

    /**
     * Equals method
     * @param o object to be compared
     * @return true if the condition is respected
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RMIServerImplementation that = (RMIServerImplementation) o;
        return Objects.equals(clients, that.clients);
    }

    /**
     * This method provides the hash code of an object
     * @return hash code
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), clients);
    }
}