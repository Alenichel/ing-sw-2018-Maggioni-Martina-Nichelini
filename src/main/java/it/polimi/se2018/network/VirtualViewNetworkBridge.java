package it.polimi.se2018.network;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
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

    BlockingQueue queue = new SynchronousQueue();

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
            e.printStackTrace();
        }

        if (this.clientAuthenticated){
            //una volta che il giocatore è autenticato lo metto nella stanza
            this.associatedVirtualView = new VirtualView(this, this.player);

            associatedVirtualView.mySetChanged();
            associatedVirtualView.notifyObservers(new ConnectionMessage(this.player, true));

            //mando il giocatore indietro
            try {
                oos.writeObject(new HandshakeConnectionMessage(player));
            }catch (IOException e){
                Logger.ERROR(LoggerType.SERVER_SIDE, ":WWNB:: " + e);
            }
            VirtualListener vl = new VirtualListener();
            Sender s = new Sender();
            vl.start();
            s.start();

        }
        else {
            try{
                oos.writeObject(new ErrorMessage("Authentication Error"));
                socket.close();
            } catch (IOException e){Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());}
        }
    }


    private void authenticateUser(String name, String password) throws AuthenticationErrorException {
        String HOME_PATH = System.getProperty("user.home");
        String DB_PATH = "/users_db.txt";
        Path path = Paths.get(HOME_PATH+DB_PATH);
        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String nextLine = reader.readLine();
            while (!nextLine.equals("end")) {
                nextLine = reader.readLine();
                String[] userInfo = nextLine.split(":");
                if (userInfo[0].equals(name)) {
                    if (userInfo[1].equals(password)){
                        this.clientAuthenticated = true;
                        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, "user " + name +  " has been authenticated");
                        this.player = new Player(name);
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
        } catch (InterruptedException e) {e.printStackTrace();}
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
        } catch (InterruptedException e){e.printStackTrace();}
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
                }
            } catch (InterruptedException | IOException e) {e.printStackTrace();}
        }
    }

    /**
     * This inner class listen for messages coming from associated client and calls for notify method of the VirtualView.
     */
    private class VirtualListener extends Thread {
        @Override
        public void run(){
            // qui deve ricevere dalla view e inoltrare al controller
            try {
                while (clientConnected && !socket.isClosed()) {
                    try {
                        Object in = ois.readObject();

                        SocketUpdateContainer packet = (SocketUpdateContainer)in;
                        associatedVirtualView.mySetChanged();
                        associatedVirtualView.notifyObservers(/*packet.getObservable(), */packet.getObject());
                    } catch (ClassNotFoundException e){
                        Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());
                        e.printStackTrace();
                    }
                }
                ois.close();
                oos.close();
                socket.close();
                System.out.println("Exited");
            }
            catch (EOFException e){
                Logger.NOTIFICATION(LoggerType.SERVER_SIDE, "VVNB_LISTENER: Socket has been close client side");
                associatedVirtualView.mySetChanged();
                associatedVirtualView.notifyObservers(new ConnectionMessage(player, false));
                return;
            }
            catch (IOException e) {
                Logger.ERROR(LoggerType.SERVER_SIDE, "VVNB_LISTENER: " + e);
                e.printStackTrace();
            }
        }
    }
}

