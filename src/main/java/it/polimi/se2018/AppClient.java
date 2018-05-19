package it.polimi.se2018;


import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.view.CliView;

import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        System.out.println("[*] Please insert your unsername");
        Scanner sinput = new Scanner(System.in);
        String input = sinput.nextLine();

        Player p = new Player(input.toString());

        System.out.println("[*] " + p.getNickname() + " welcome to Sagrada");

        CliView cw = new CliView(input.toString());

        SocketClient sc = new SocketClient("localhost", 9091, cw);
        cw.addObserver(sc);
        cw.run();
    }
}
