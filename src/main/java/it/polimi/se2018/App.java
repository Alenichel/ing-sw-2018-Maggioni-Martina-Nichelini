package it.polimi.se2018;

import it.polimi.se2018.network.SocketServer;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Logger.setSide(LoggerType.SERVER_SIDE);
        SocketServer ss = new SocketServer();
        ss.run();
    }
}
