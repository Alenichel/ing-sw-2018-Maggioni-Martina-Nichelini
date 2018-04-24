package it.polimi.se2018;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

public class SocketServer {

    public static void main(String[] args) throws IOException {

        System.out.println( "Hello World!" );

        ServerSocket listener = new ServerSocket(9090);

        Socket socket = listener.accept();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        socket.close();
        listener.close();

        System.out.println( "Farewell" );
    }
}

