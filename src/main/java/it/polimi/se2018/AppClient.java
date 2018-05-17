package it.polimi.se2018;


import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.view.CliView;

public class AppClient {

    public static void main(String[] args) {
        Player p = new Player("Alenichel");
        CliView cw = new CliView(p);
        SocketClient sc = new SocketClient("localhost", 9091, cw);
        cw.addObserver(sc);
        cw.run();
    }
}
