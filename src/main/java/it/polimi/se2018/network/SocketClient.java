package it.polimi.se2018.network;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.view.View;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * This class works as SocketClient of client game side. It listens for messages coming from the server and
 * it observes RealView for changes to notify them trough the network.
 */
public class SocketClient implements Observer {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean keepAlive;
    BlockingQueue queue = new SynchronousQueue();


    private View associatedView;

    /**
     * Class constructor
     */
    public SocketClient(String serverIP, int port, String nickname, String password , View associatedView) throws AuthenticationErrorException, IOException, ClassNotFoundException {
        try {
            this.keepAlive = true;
            this.associatedView = associatedView;

            socket = new Socket(serverIP, port);
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Socket ready..\n");
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Connection established\n");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(new HandshakeConnectionMessage(nickname, password));

            Object rcv = ois.readObject();
            if (rcv instanceof HandshakeConnectionMessage)
                associatedView.setPlayer(((HandshakeConnectionMessage)rcv).getPlayer());
            else {
                throw new AuthenticationErrorException(rcv.toString());
            }

        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }

        Listener listener = new Listener();
        Sender sender = new Sender();
        sender.start();
        listener.start();
    }

    /**
     * Inner class that keeps listening for notification from the server
     */
    private class Listener extends Thread{

        @Override
        public void run(){
            Object in = null;
            while (!socket.isClosed() && keepAlive){
                try {
                    in = ois.readObject();
                } catch (EOFException e){
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Connection was closed server side. Closing...");
                    System.exit(0);
                }
                catch (StreamCorruptedException e){
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, ":SOCKETCLIENT_LISTENER: caught java.io.StreamCorruptedException");
                    break;
                }
                catch (ClassNotFoundException | IOException e){
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, ":SOCKETCLIENT_LISTENER:" +  e.toString());
                    System.exit(1);
                }
                //update from model (network)
                if (in instanceof SocketUpdateContainer){
                    SocketUpdateContainer suc = (SocketUpdateContainer)in;
                    associatedView.update(suc.getObservable(), suc.getObject());
                }
                //update from controller
                else if (in instanceof Message){
                    associatedView.controllerCallback((Message)in);
                }
            }
        }
    }

    /**
     * Inner class sender
     */
    private class Sender extends Thread{
        @Override
        public void run() {
            try {
                Message msg;
                while (keepAlive) {
                    msg = (Message) queue.take();
                    oos.writeObject(msg);
                    oos.flush();
                    oos.reset();
                }
            } catch (InterruptedException | IOException e) {
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            }
        }
    }

    /**
     * This method implements Observer interface and simply takes updating params and sends them trough the network.
     */
    public void update(Observable o, Object msg) {
        SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
        try {
            queue.put(suc);
        } catch (InterruptedException e) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR,"::SC_UPDATE: InterruptedException");
            Thread.currentThread().interrupt();
        }
    }
}