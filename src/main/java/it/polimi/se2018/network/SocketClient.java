package it.polimi.se2018.network;

import it.polimi.se2018.message.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * This class works as SocketClient of client game side. It listens for messages coming from the server and
 * it observes RealView for changes to notify them trough the network.
 */
public class SocketClient implements Observer {

    private String serverIP;
    private int port;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean clientConnected;
    BlockingQueue queue = new SynchronousQueue();


    private View associatedView;

    public SocketClient(String serverIP, int port, String nickname, String password ,View associatedView) {
        try {
            this.serverIP = serverIP;
            this.port = port;
            this.associatedView = associatedView;

            socket = new Socket(serverIP, port);
            Logger.log(LoggerType.CLIENT_SIDE, "[*] Socket ready..\n");
            Logger.log(LoggerType.CLIENT_SIDE, "[*] Connection established\n");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(new HandshakeConnectionMessage(nickname, password));

            Object rcv = ois.readObject();
            if (rcv instanceof HandshakeConnectionMessage)
                ((CliView) associatedView).setPlayer(((HandshakeConnectionMessage)rcv).getPlayer());
            else {
                Logger.log(LoggerType.CLIENT_SIDE, rcv.toString());
                System.exit(1);
            }

        } catch (IOException | ClassNotFoundException e) {
            Logger.ERROR(LoggerType.CLIENT_SIDE,e.toString());
            e.printStackTrace();
            System.exit(1);
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
            while (!socket.isClosed()){
                try {
                    in = ois.readObject();
                } catch (ClassNotFoundException | IOException e){
                    Logger.ERROR(LoggerType.CLIENT_SIDE, e.toString());
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

    private class Sender extends Thread{
        @Override
        public void run() {
            try {
                Message msg = new UpdateMessage("ciao");
                while (msg.getMessageType() != "quit") {
                    msg = (Message) queue.take();
                    oos.writeObject(msg);
                    oos.flush();
                    oos.reset();
                }
            } catch (InterruptedException | IOException e) {
                Logger.ERROR(LoggerType.CLIENT_SIDE, e.toString());
                e.printStackTrace();
            }
        }
    }


    /**
     * This method implements Observer interface and simply take updating params and sends it trough the network.
     * @param o
     * @param msg
     */
    public void update(Observable o, Object msg) {
        SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
        try {
            queue.put(suc);
        } catch (InterruptedException e) {
            Logger.ERROR(LoggerType.CLIENT_SIDE, "::SC_UPDATE: InterruptedException");}
    }
}


