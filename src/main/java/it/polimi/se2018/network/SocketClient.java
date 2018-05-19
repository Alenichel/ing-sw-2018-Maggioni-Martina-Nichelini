package it.polimi.se2018.network;

import it.polimi.se2018.message.*;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class SocketClient extends Thread implements Observer {

    private String serverIP;
    private int port;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean clientConnected;
    Scanner stdin = new Scanner(System.in);

    private View associatedView;

    public SocketClient(String serverIP, int port, View associatedView) {
        try {
            this.serverIP = serverIP;
            this.port = port;
            this.associatedView = associatedView;

            socket = new Socket(serverIP, port);
            System.out.println("[*] Socket ready..");
            System.out.println("[*] Connection established");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(new HandshakeConnectionMessage(((CliView)associatedView).getPlayername(), "password"));
            try {
                //dopo aver ricevuto il giocatore autenticato dal server lo associo alla ClieView
                ((CliView) associatedView).setPlayer(((HandshakeConnectionMessage)ois.readObject()).getPlayer());
            }catch (ClassNotFoundException e){
                System.out.print(e);
            }
        } catch (IOException e) {
            System.out.println(e + "/n" + "[*] Error, exiting..");
        }
        Listener listener = new Listener();
        listener.start();

    }


    private class Listener extends Thread{

        public void run(){
            Object in = null;
            while (!socket.isClosed()){
                try {
                    in = ois.readObject();
                } catch (ClassNotFoundException | IOException e){System.out.println(e);}

                if (in instanceof SocketUpdateContainer){
                    SocketUpdateContainer suc = (SocketUpdateContainer)in;
                    associatedView.update(suc.getObservable(), suc.getObject());
                }
                else if (in instanceof Message){
                    associatedView.controllerCallback((Message)in);
                }
            }
        }
    }

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


