package it.polimi.se2018.network;

import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.utils.Security;
import it.polimi.se2018.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is the bridge between the virtual view and the rest of the network.
 */
public class SocketServerImplementation extends Thread implements ServerInterface {
    //OLD VIRTUALVIEW NETWORK BRIDGE
    private Socket socket;
    private boolean clientConnected = true;
    private boolean clientAuthenticated;
    private AtomicBoolean isClosing = new AtomicBoolean();

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private VirtualView associatedVirtualView;
    private Player player;

    BlockingQueue queue = new LinkedBlockingQueue();

    public SocketServerImplementation(Socket socket){
        this.socket = socket;
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            HandshakeConnectionMessage hcm = (HandshakeConnectionMessage) this.ois.readObject();
            this.player = Security.authenticateUser(hcm.getUsername(), hcm.getEncodedPassword());

        }
        catch (AuthenticationErrorException e ) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, ":WWNB_CONSTRUCTOR: Unidentified user tried to log in");
            }
        catch (IOException | ClassNotFoundException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, ":WWNB_CONSTRUCTOR: " + e);
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
        }

        if (this.player != null){
            //once the player is authenticated, I put him in the setupping game.
            this.associatedVirtualView = new VirtualView(this, this.player);
            this.player.setVv(associatedVirtualView);
            associatedVirtualView.mySetChanged();
            associatedVirtualView.notifyObservers(new ConnectionMessage(this.player, true));

            //sending back the instance of the player
            try {
                oos.writeObject(new HandshakeConnectionMessage(player));
            }catch (IOException /*| InterruptedException*/ e){
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, ":WWNB:: " + e);
            }
        }
        else {
            try{
                UpdateMessage um = new UpdateMessage(null);
                um.setStringMessage("Authentication error");
                oos.writeObject(um);
                socket.close();
            } catch (IOException e){
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
            }
        }
    }

    /**
     * This method handles the callbacks of controllers on the virtualView. Each callback is passed from VirtualView
     * to this method and through this, to the network.
     * @param callbackMessage This is the message passed from the controller.
     */
    public void controllerCallback(Message callbackMessage){
        try {
            queue.put(callbackMessage);
        } catch (InterruptedException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, e.toString());
            Thread.currentThread().interrupt();
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
            queue.put(suc);
        } catch (InterruptedException e){
            if (! (msg instanceof UpdateMessage && ((UpdateMessage)msg).getWhatToUpdate().equals(WhatToUpdate.TimeLeft) ))
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, "SSI: "+e.toString());
            Thread.currentThread().interrupt();
        }
    }

    private class Sender extends Thread{
        @Override
        public void run() {
            try {
                Message msg;
                do {
                    msg = (Message) queue.take();
                    try { oos.writeObject(msg);}
                    catch (ConcurrentModificationException e) {
                        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING,  e.toString());
                    }
                    oos.flush();
                    oos.reset();
                } while (msg.getMessageType() != "quit" &&  clientConnected);
            } catch (InterruptedException | IOException e) {
                if (!isClosing.get()) Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, e.toString());
            }
        }
    }

    /**
     * This inner class listen for messages coming from associated client and calls for notify method of the VirtualView.
     */
    private class VirtualListener extends Thread {
        @Override
        public void run(){
            try {
                while (clientConnected && !socket.isClosed()) {
                    try {
                        Object in = ois.readObject();

                        SocketUpdateContainer packet = (SocketUpdateContainer)in;
                        associatedVirtualView.mySetChanged();
                        associatedVirtualView.notifyObservers(/*packet.getObservable(), */packet.getObject());
                    } catch (ClassNotFoundException e){
                        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
                    }
                }
                ois.close();
                oos.close();
                socket.close();
            }
            catch (EOFException e){ //endOfFIle
                Server server = Server.getInstance();
                isClosing.set(true);
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING,"VVNB_LISTENER: Socket (for user: " + player.getNickname() +") has been closed client side");
                associatedVirtualView.mySetChanged();
                associatedVirtualView.notifyObservers(new ConnectionMessage(player, false));
                clientConnected = false;
                return;
            }
            catch (SocketException e){
                Server server = Server.getInstance();
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING, "VVNB_LISTENER: " + e.toString());
                associatedVirtualView.mySetChanged();
                associatedVirtualView.notifyObservers(new ConnectionMessage(player, false));
                clientConnected = false;
                return;
            }
            catch (IOException e) {
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, "VVNB_LISTENER: " + e.toString());
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));

            }
        }
    }

    @Override
    public void run(){
        VirtualListener vl = new VirtualListener();
        Sender s = new Sender();
        vl.start();
        s.start();
    }



    // ---------------> Come risolvere ?
    public void receiver(Observable o, Object msg){
        ;
    }

    @Override
    public void addClient(ClientInterface client) throws RemoteException {
        System.out.println("ciao");
    }

    @Override
    public void pong(){
        ;
    }
}

