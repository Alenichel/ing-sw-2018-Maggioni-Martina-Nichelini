package it.polimi.se2018;


import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.CliView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class AppClient extends Application{

    public static void main(String[] args) {
        Logger.setSide(LoggerType.CLIENT_SIDE);
        Scanner inputInit = new Scanner(System.in);
        boolean cli;

        System.out.print("[*] Insert: \n\t1 for Cli\n\t2 for Gui\n");
        int chosen = inputInit.nextInt();
        if(chosen == 1) cli = true;
        else cli = false;

        Scanner sinput = new Scanner(System.in);

        if(cli) {
            int network = 0;
            while (network != 1 && network != 2) {
                System.out.print("[*] Insert: \n\t1 for Socket\n\t2 for RMI\n");
                network = inputInit.nextInt();
            }

            System.out.print("[*] Please insert your username: ");
            String input = sinput.nextLine();

            String nickname = input.toString();

            String password = "";
            if (Server.getInstance().isConfigurationRequired()) {
                System.out.print("\n[*] Please insert your password: ");
                password = sinput.nextLine();
            }

            CliView cw = new CliView();

            if (network == 1) {
                SocketClient sc = new SocketClient("localhost", 9091, nickname, password, cw);
                cw.addObserver(sc);
            }

            if (network == 2) {
                RMIClient rmiClient = new RMIClient();
                cw.addObserver(rmiClient.run(cw, nickname, password));
            }
            cw.run();
        }else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

        primaryStage.setTitle("Welcome!");
        Scene sceneLogin = new Scene(root, 620, 300);

        primaryStage.setScene(sceneLogin);
        primaryStage.show();
    }
}
