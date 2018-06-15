package it.polimi.se2018.network;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SocketUpdateContainer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.view.View;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public class RMIClientImplementation implements ClientInterface, Observer {

	private final String insertedNickname;
	private final String insertedPassword;
	private final View associatedView;
	private final ServerInterface server;

	public RMIClientImplementation(View view, String nickname, String password, ServerInterface server){
		this.insertedNickname = nickname;
		this.insertedPassword = password;
		this.associatedView = view;
		this.server = server;
	}

	@Override
	public String getInsertedNickname(){
		return this.insertedNickname;
	}

	@Override
	public String getInsertedPassword(){
		return this.insertedPassword;
	}

	@Override
	public void setPlayer(Player player){
		this.associatedView.setPlayer(player);
	}

	/**
	 * This method calls the server corresponding method.
	 * @param observable
	 * @param message
	 */
	@Override
	public void update(Observable observable, Object message){
		try {
			((Message)message).setSignedBy(insertedNickname);
			server.receiver(observable, message);
		} catch (RemoteException e) {
			Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, e.toString());
		}
	}

	@Override
	public void notify(Message message) throws RemoteException {
		if (message instanceof SocketUpdateContainer){
			SocketUpdateContainer suc = (SocketUpdateContainer)message;
			associatedView.update(suc.getObservable(), suc.getObject());
		}
		//update from controller
		else if (message instanceof Message){
			associatedView.controllerCallback((Message)message);
		}
	}
}
