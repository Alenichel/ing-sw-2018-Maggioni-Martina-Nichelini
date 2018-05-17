package it.polimi.se2018;

import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.SocketServer;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        SocketServer ss = new SocketServer();
        ss.run();
    }
}
