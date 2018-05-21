package it.polimi.se2018.network;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Room;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

/**
 * This class is the bridge between the virtual view and the rest of the network.
 */
public class VirtualViewNetworkBridge extends Thread {

    private Socket socket;
    private boolean clientConnected = true;
    private boolean clientAuthenticated;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private VirtualView associatedVirtualView;
    private Player player;

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

            //una volta che il giocatore è autenticato lo metto nella stanza
            player.setRoom(Room.getInstance(), true);

            //mando il giocatore indietro
            Server.getInstance().addPlayer(this.player);
            try {
                oos.writeObject(new HandshakeConnectionMessage(player));
            }catch (IOException e){
                System.out.println(e);
            }
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

    private void authenticateUser(String name, byte[] password){
        //controllare che il player esista. aggiungere funzione adibitia
        this.clientAuthenticated = true;
        this.player= new Player(name);


    }

    /**
     * This method handles the callbacks of controllers on the virtualView. Each callback is passed from VirtualView
     * to this method and through this, to the network.
     * @param callbackMessage This is the message passed from the controller.
     */
    public void controllerCallback(Message callbackMessage){
        try {
            this.oos.writeObject(callbackMessage);
            this.oos.flush();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * This method handles update messages coming from model classes directed to the registered virtual views.
     * Each VirtualView's update method call triggered this method that pass messages to the network.
     * @param o Canonical param of Observer.Update method.
     * @param msg Canonical param of Observer.Update method.
     */
    public void update(Observable o, Object msg){
        SocketUpdateContainer suc = new SocketUpdateContainer(o, msg);
        try {
            this.oos.writeObject(suc);
            this.oos.flush();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * This inner class listen for messages coming from associated client and calls for notify method of the VirtualView.
     */
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

