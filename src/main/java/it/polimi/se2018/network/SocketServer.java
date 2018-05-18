package it.polimi.se2018.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {

    private ServerSocket ssocket;

    @Override
    public void run() {
        try {
            ssocket = new ServerSocket(9091);
            System.out.println("Listening");
            while (true) {
                Socket socket = this.ssocket.accept();
                System.out.println("[*] NOTIFICATION: new client connected");
                VirtualViewNetworkBridge virtualClientThread;
                virtualClientThread = new VirtualViewNetworkBridge(socket);
                virtualClientThread.start();
            }
        } catch (IOException e){
            assert false;
        }
    }
}



