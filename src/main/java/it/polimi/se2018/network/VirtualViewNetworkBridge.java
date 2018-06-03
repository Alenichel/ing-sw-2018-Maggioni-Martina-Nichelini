package it.polimi.se2018.network;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

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

    BlockingQueue queue = new LinkedBlockingQueue();

    public VirtualViewNetworkBridge(Socket socket){
        this.socket = socket;
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            HandshakeConnectionMessage hcm = (HandshakeConnectionMessage) this.ois.readObject();
            authenticateUser(hcm.getUsername(), hcm.getEncodedPassword());

        }
        catch (AuthenticationErrorException e ) {
            Logger.WARNING(LoggerType.SERVER_SIDE, ":WWNB_CONSTRUCTOR: Unidentified user tried to log in");
            }
        catch (IOException | ClassNotFoundException e){
            Logger.ERROR(LoggerType.SERVER_SIDE, ":WWNB_CONSTRUCTOR: " + e);
            Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
        }

        if (this.clientAuthenticated){
            //once the player is authenticated, I put him in the setupping game.
            this.associatedVirtualView = new VirtualView(this, this.player);

            associatedVirtualView.mySetChanged();
            associatedVirtualView.notifyObservers(new ConnectionMessage(this.player, true));

            //sending back the instance of the player
            try {

                oos.writeObject(new HandshakeConnectionMessage(player));
            }catch (IOException /*| InterruptedException*/ e){
                Logger.ERROR(LoggerType.SERVER_SIDE, ":WWNB:: " + e);
            }
        }
        else {
            try{
                oos.writeObject(new
                        ErrorMessage("Authentication Error"));
                socket.close();
            } catch (IOException e){Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());}
        }
    }

    /**
     * This method check if the server contains an instance of a player with the same name of the one
     * provided. If true, it returns that instance, otherwise it create a new player whit that name and
     * returns it.
     * @param name of the player to generate
     * @return
     */
    private Player generateUser(String name) throws  AuthenticationErrorException{
        for ( Player p: Server.getInstance().getOnlinePlayers() )
            if (p.getNickname().equals(name)) {
            throw new AuthenticationErrorException("UserAlreadyConnected");
            }

        for ( Player p : Server.getInstance().getOfflinePlayers() ){
            if ( p.getNickname().equals(name)) return p;
        }
        return new Player(name);
    }

    private void authenticateUser(String name, String password) throws AuthenticationErrorException {
        String DB_PATH = "/sagrada_users_db.txt";
        Path path = Paths.get("resources" + DB_PATH);
        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String nextLine = reader.readLine();
            while (!nextLine.equals("end")) {
                nextLine = reader.readLine();
                String[] userInfo = nextLine.split(":");
                if (userInfo[0].equals(name)) {
                    try {
                        if (Server.getInstance().isConfigurationRequired()) {
                            if (userInfo[1].equals(password)) {
                                this.clientAuthenticated = true;
                                Logger.NOTIFICATION(LoggerType.SERVER_SIDE, "user " + name + " has been authenticated");
                                this.player = generateUser(name);
                                return;
                            }
                        } else {
                            this.clientAuthenticated = true;
                            this.player = generateUser(name);
                        }
                    } catch (AuthenticationErrorException e) {
                        this.clientAuthenticated = false;
                        return;
                    }
                }
            }
            throw new AuthenticationErrorException("UserNotFound");
        } catch (IOException e){Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());}

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
            Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());
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
            Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());
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
                        Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());
                        sleep(100);
                        oos.writeObject(msg);
                    }
                    oos.flush();
                    oos.reset();
                } while (msg.getMessageType() != "quit" &&  clientConnected);
            } catch (InterruptedException | IOException e) { Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());}

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
                        Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
                        Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());
                    }
                }
                ois.close();
                oos.close();
                socket.close();
            }
            catch (EOFException e){
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING,"VVNB_LISTENER: Socket (for user: " + player.getNickname() +" )has been closed client side");
                associatedVirtualView.mySetChanged();
                associatedVirtualView.notifyObservers(new ConnectionMessage(player, false));
                clientConnected = false;
                return;
            }
            catch (IOException e) {
                Logger.ERROR(LoggerType.SERVER_SIDE, "VVNB_LISTENER: " + e);
                Logger.WARNING(LoggerType.SERVER_SIDE, e.toString());
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
}

