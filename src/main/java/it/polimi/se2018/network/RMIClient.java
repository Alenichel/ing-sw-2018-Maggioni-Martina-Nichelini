package it.polimi.se2018.network;

import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.View;
import javafx.beans.Observable;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observer;

public class RMIClient {

	public static RMIClientImplementation run(View view, String nickname, String password) {
		ServerInterface server;
        RMIClientImplementation client = null;
		try {
			server = (ServerInterface)Naming.lookup("//localhost/MyServer");
			client = new RMIClientImplementation(view, nickname, password, server);
			ClientInterface remoteRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
			server.addClient(remoteRef);
            return client;
		} catch (MalformedURLException e) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Not found URL");
		} catch (RemoteException e) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Connection Error: " + e.getMessage() + "!");
		} catch (NotBoundException e) {
			Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Il riferimento passato non è associato a nulla!");
		}
        return client;
	}

}
