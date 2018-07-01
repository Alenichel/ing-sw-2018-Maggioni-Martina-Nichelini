package it.polimi.se2018.network;

import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.view.View;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIClient {

	public static RMIClientImplementation run(View view, String serverURL,String nickname, String password) {
		ServerInterface server;
        RMIClientImplementation client = null;
		try {
			server = (ServerInterface)Naming.lookup("//" + serverURL + "/SagradaServer");
			client = new RMIClientImplementation(view, nickname, password, server);
			ClientInterface remoteRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);

			try {
				server.addClient(remoteRef);
			} catch (Exception e) {
				return null;
			}

			Ping pinger = new Ping(server);
			pinger.start();
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

	private static class Ping extends Thread{

	    private ServerInterface si;

	    private Ping(ServerInterface si){
	        this.si = si;
        }

	    @Override
        public void run(){
	        while (true){
	            try {
                    sleep((long) 5 * 1000);
                    si.pong();
                } catch (RemoteException e){
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Server seems to have died. Closing..");
                    System.exit(1);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

}
