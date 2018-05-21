package it.polimi.se2018.network;

import it.polimi.se2018.message.*;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * This class works as SocketClient of client game side. It listens for messages coming from the server and
 * it observes RealView for changes to notify them trough the network.
 */
public class SocketClient extends Thread implements Observer {

    private String serverIP;
    private int port;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean clientConnected;
    Scanner stdin = new Scanner(System.in);

    private View associatedView;

    public SocketClient(String serverIP, int port, String nickname, String password ,View associatedView) {
        try {
            this.serverIP = serverIP;
            this.port = port;
            this.associatedView = associatedView;

            socket = new Socket(serverIP, port);
            System.out.println("[*] Socket ready..");
            System.out.println("[*] Connection established");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(new HandshakeConnectionMessage(nickname, password));

            Object rcv = ois.readObject();
            if (rcv instanceof HandshakeConnectionMessage)
                ((CliView) associatedView).setPlayer(((HandshakeConnectionMessage)rcv).getPlayer());
            else {
                System.out.println(rcv);
                System.exit(1);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[*] Error: " + e +  " exiting..");
            System.exit(1);
        }

        Listener listener = new Listener();
        listener.start();
    }

    /**
     * Inner class that keeps listening for notification from the server
     */
    private class Listener extends Thread{

        public void run(){
            Object in = null;
            while (!socket.isClosed()){
                try {
                    in = ois.readObject();
                } catch (ClassNotFoundException | IOException e){
                    System.out.println(e);
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
     * This method implements Observer interface and simply take updating params and sends it trough the network.
     * @param o
     * @param msg
     */
    public void update(Observable o, Object msg) {
        SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
        try {
            this.oos.writeObject(suc);
            this.oos.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}


