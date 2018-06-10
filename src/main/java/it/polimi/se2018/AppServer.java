package it.polimi.se2018;

import it.polimi.se2018.network.RMIServer;
import it.polimi.se2018.network.SocketServer;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;

public class AppServer {
    public static void main(String[] args) {
        Logger.setSide(LoggerType.SERVER_SIDE, false);
        SocketServer ss = new SocketServer();
        ss.start();
        RMIServer rmiServer = new RMIServer();
    }
}
