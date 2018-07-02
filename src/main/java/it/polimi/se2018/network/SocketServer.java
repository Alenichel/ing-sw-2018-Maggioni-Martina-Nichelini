package it.polimi.se2018.network;

import it.polimi.se2018.model.Server;

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
            System.out.println("Listening");
            while (true) {
                Socket socket = this.ssocket.accept();
                System.out.println("[*] NOTIFICATION: new client connected");
                SocketServerImplementation virtualClientThread;
                virtualClientThread = new SocketServerImplementation(socket);
                virtualClientThread.start();
            }
        } catch (IOException e){
            assert false;
        }
    }
}



