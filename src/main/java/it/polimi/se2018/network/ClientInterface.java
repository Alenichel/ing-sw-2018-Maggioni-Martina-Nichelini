package it.polimi.se2018.network;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.model.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client interface class
 */
public interface ClientInterface extends Remote {
    String getInsertedNickname() throws RemoteException;
    String getInsertedPassword() throws RemoteException;
    void setPlayer(Player player) throws RemoteException;
    void notify(Message message) throws RemoteException;
}
