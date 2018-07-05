package it.polimi.se2018.network;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class handles new incoming socket connection. For each new client, a new thread is created.
 */
public class SocketServer extends Thread {

    private ServerSocket ssocket;

    @Override
    public void run() {
        try {
            ssocket = new ServerSocket(Server.getInstance().getServerPort());
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NORMAL, "Listening");
            while (true) {
                Socket socket = this.ssocket.accept();
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NORMAL,"[*] NOTIFICATION: new client connected");
                SocketServerImplementation virtualClientThread;
                virtualClientThread = new SocketServerImplementation(socket);
                virtualClientThread.start();
            }
        } catch (IOException e){
            assert false;
        }
    }
}