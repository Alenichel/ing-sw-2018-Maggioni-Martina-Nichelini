package it.polimi.se2018.network;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.utils.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Class for RMI Server
 */
public class RMIServer {

    private static int PORT = 1099; //default port

	/**
	 * Class constructor
	 */
	public RMIServer() {
		try {
			LocateRegistry.createRegistry(PORT);
		} catch (RemoteException e) {
			Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR,"There is already a register");
		}
		try {
			RMIServerImplementation serverImplementation = new RMIServerImplementation();
			Naming.rebind("//localhost/SagradaServer", serverImplementation);
		} catch (MalformedURLException e) {
			Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR,"Impossible to register the given object");
		} catch (RemoteException e) {
			Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR,"Connection error: " + e.getMessage() + "!");
		}
	}
}