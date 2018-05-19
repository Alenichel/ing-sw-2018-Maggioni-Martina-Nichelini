package it.polimi.se2018.network;

import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

//virtual client or socket thread
public class VirtualViewNetworkBridge extends Thread {

    private Socket socket;
    private boolean clientConnected = true;
    private boolean clientAuthenticated;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private VirtualView associatedVirtualView;
    private Player player;

    private void authenticateUser(String name, byte[] password){
        //controllare che il player esista. aggiungere funzione adibitia
        this.clientAuthenticated = true;
        this.player= new Player(name);
        Server.getInstance().addPlayer(this.player);
        try {
            oos.writeObject(new HandshakeConnectionMessage(player));
        }catch (IOException e){
            System.out.println(e);
        }

    }

    public VirtualViewNetworkBridge(Socket socket){
        this.socket = socket;
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            HandshakeConnectionMessage hcm = (HandshakeConnectionMessage) this.ois.readObject();
            authenticateUser(hcm.getUsername(), hcm.getEncodedPassword());

        } catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        if (this.clientAuthenticated){
            System.out.println("[*] NOTIFICATION: User in now authenticated");
            this.associatedVirtualView = new VirtualView(this, this.player);
            VirtualListener vl = new VirtualListener();
            vl.start();
        }
        else {
            System.out.println("[*] ERROR: Authentication error");
            try{
                socket.close();
            } catch (IOException e){System.out.println(e);}
        }
    }

    //Prende un messaggio inviato dal controller al model
    public void controllerCallback(Message callbackMessage){
        try {
            this.oos.writeObject(callbackMessage);
            this.oos.flush();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // Prende i parametri dalla rete e li scrive sul canale delle socket.
    public void update(Observable o, Object msg){
        SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
        try {
            this.oos.writeObject(suc);
            this.oos.flush();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    public class VirtualListener extends Thread {
        @Override
        public void run(){
            // qui deve ricevere dalla view e inoltrare al controller.
            try {
                while (clientConnected && !socket.isClosed()) {
                    try {
                        Object in = ois.readObject();

                        SocketUpdateContainer packet = (SocketUpdateContainer)in;
                        associatedVirtualView.mySetChanged();
                        associatedVirtualView.notifyObservers(/*packet.getObservable(), */packet.getObject());
                    } catch (ClassNotFoundException e){
                        System.out.println(e);
                    }
                }
                ois.close();
                oos.close();
                socket.close();
                System.out.println("Exited");
            }
            catch (IOException e) {System.out.println(e); }
        }
    }
}

