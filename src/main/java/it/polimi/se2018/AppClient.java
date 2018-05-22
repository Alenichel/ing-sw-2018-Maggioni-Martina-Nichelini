package it.polimi.se2018;


import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.view.CliView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        Scanner sinput = new Scanner(System.in);

        System.out.print("[*] Please insert your username: ");
        String input = sinput.nextLine();

        String nickname = input.toString();
        //byte[] encodedHash = null;

        System.out.print("\n[*] Please insert your password: ");
        input = sinput.nextLine();
        /*try { //read password from STDIN and apply hash function

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedHash= digest.digest(
                    input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {System.out.println(e);}*/

        System.out.println("[*] " + nickname + " welcome to Sagrada");

        CliView cw = new CliView();
        SocketClient sc = new SocketClient("localhost", 9091, nickname, input, cw);
        cw.addObserver(sc);
        cw.run();
    }
}
