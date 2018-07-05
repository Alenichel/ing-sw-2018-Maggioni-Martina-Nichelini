package it.polimi.se2018.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

import it.polimi.se2018.message.Message;

public interface ServerInterface extends Remote {
	
    void addClient(ClientInterface client) throws RemoteException;

    void pong() throws RemoteException;


    void controllerCallback(Message callbackMessage)throws RemoteException;

    void update(Observable o, Object msg)throws RemoteException;

    void receiver(Observable o, Object msg) throws  RemoteException;

}