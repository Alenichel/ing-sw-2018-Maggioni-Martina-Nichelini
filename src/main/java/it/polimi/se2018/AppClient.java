package it.polimi.se2018;


import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.GuiView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

import static javafx.application.Application.launch;

public class AppClient extends Application{

    public static void main(String[] args) {
        Logger.setSide(LoggerType.CLIENT_SIDE);
        Scanner inputInit = new Scanner(System.in);
        boolean cli;

        System.out.print("[*] Insert 1 for Cli or 2 for Gui: ");
        int chosen = inputInit.nextInt();
        if(chosen == 1) cli = true;
        else cli = false;

        Scanner sinput = new Scanner(System.in);

        if(cli) {
            System.out.print("[*] Please insert your username: ");
            String input = sinput.nextLine();

            String nickname = input.toString();

            if (Server.getInstance().isConfigurationRequired()) {
                System.out.print("\n[*] Please insert your password: ");
                input = sinput.nextLine();
            } else {
                input = "null";
            }

            System.out.println("[*] " + nickname + " welcome to Sagrada");

            CliView cw = new CliView();
            SocketClient sc = new SocketClient("localhost", 9091, nickname, input, cw);
            cw.addObserver(sc);
            cw.run();
        }else {

            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        GuiView gw = new GuiView();
        gw.setupLoginView(primaryStage);




    }
}
