package it.polimi.se2018;



import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.CliView;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        Logger.setSide(LoggerType.CLIENT_SIDE);
        Scanner sinput = new Scanner(System.in);

        System.out.print("[*] Please insert your username: ");
        String input = sinput.nextLine();

        String nickname = input.toString();

        System.out.print("\n[*] Please insert your password: ");
        input = sinput.nextLine();

        System.out.println("[*] " + nickname + " welcome to Sagrada");

        CliView cw = new CliView();
        SocketClient sc = new SocketClient("localhost", 9091, nickname, input, cw);
        cw.addObserver(sc);
        cw.run();
    }
}
