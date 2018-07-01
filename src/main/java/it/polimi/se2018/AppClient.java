package it.polimi.se2018;


import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.view.CliView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Scanner;

public class AppClient extends Application{

    public static void main(String[] args) {
        Logger.setSide(LoggerType.CLIENT_SIDE, false);
        Scanner inputInit = new Scanner(System.in);
        boolean cli;

        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"[*] Insert: \n\t1 for Cli\n\t2 for Gui");
        int chosen = Integer.parseInt(inputInit.nextLine());
        if(chosen == 1) cli = true;
        else cli = false;


        if(cli) {
            int network = 0;
            while (network != 1 && network != 2) {
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"[*] Insert: \n\t1 for Socket\n\t2 for RMI");
                network = Integer.parseInt(inputInit.nextLine());
            }

            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"[*] Please, insert server URL or leave blank for localhost:\n");
            String serverURL = inputInit.nextLine();
            if (serverURL.equalsIgnoreCase("")) serverURL = "localhost";

            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"[*] Please, insert server PORT or leave blank for the default port:\n");
            String serverPORT = inputInit.nextLine();
            if (serverPORT.equalsIgnoreCase("")) serverPORT = "9091";

            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"[*] Please insert your username: ");
            String nickname = inputInit.nextLine();


            String pass = "";
            if (Server.getInstance().isConfigurationRequired()) {
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,"\n[*] Please insert your password: ");
                pass = inputInit.nextLine();
            }

            CliView cw = new CliView();

            if (network == 1) {
                SocketClient sc = new SocketClient(serverURL, Integer.parseInt(serverPORT), nickname, pass, cw);
                cw.addObserver(sc);
            }

            if (network == 2) {
                RMIClient rmiClient = new RMIClient();
                cw.addObserver(rmiClient.run(cw, serverURL, nickname, pass));
            }
            cw.run();

        } else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

        primaryStage.setTitle("Welcome!");
        Scene sceneLogin = new Scene(root);
        String url = "/logo.png";
        Image image = new Image(url);
        primaryStage.getIcons().add(image);

        primaryStage.setScene(sceneLogin);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
