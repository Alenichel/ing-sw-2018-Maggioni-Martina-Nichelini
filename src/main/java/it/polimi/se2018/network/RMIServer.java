package it.polimi.se2018.network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {

    private static int PORT = 1099; // porta di default
	
	public RMIServer() {
		try {
			LocateRegistry.createRegistry(PORT);
		} catch (RemoteException e) {
			System.out.println("There is already a register");
		}
		try {
			RMIServerImplementation serverImplementation = new RMIServerImplementation();
			Naming.rebind("//localhost/MyServer", serverImplementation);
		} catch (MalformedURLException e) {
			System.err.println("Impossible to register the given object");
		} catch (RemoteException e) {
			System.err.println("Connection error: " + e.getMessage() + "!");
		}		

	}

}
