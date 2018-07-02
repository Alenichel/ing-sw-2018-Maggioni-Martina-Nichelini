package it.polimi.se2018;


import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.RMIClientImplementation;
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
import java.rmi.RemoteException;
import java.util.Scanner;

public class AppClient extends Application{

    public static void main(String[] args) {
        Logger.setSide(LoggerType.CLIENT_SIDE, false);
        Scanner inputInit = new Scanner(System.in);

        int cli = 0;

        while (cli != 1 && cli != 2){
            try {
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Insert: \n\t1 for Cli\n\t2 for Gui");
                cli = Integer.parseInt(inputInit.nextLine());
            } catch (NumberFormatException e){
                cli = 0;
            }
        }


        if(cli == 1) {
            int network = 0;
            String serverURL = null;
            String serverPORT = null;
            CliView cw;

            while (true) {

                while (network != 1 && network != 2) {
                    try {
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Insert: \n\t1 for Socket\n\t2 for RMI");
                        network = Integer.parseInt(inputInit.nextLine());
                    } catch (NumberFormatException e) {
                        cli = 0;
                    }
                }

                if (serverURL == null) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Please, insert server URL or leave blank for localhost:");
                    serverURL = inputInit.nextLine();
                    if (serverURL.equalsIgnoreCase("")) serverURL = "localhost";
                }

                if (serverPORT == null ) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Please, insert server PORT or leave blank for the default port:");
                    serverPORT = inputInit.nextLine();
                    if (serverPORT.equalsIgnoreCase("")) serverPORT = String.valueOf(Server.getInstance().getServerPort());
                }

                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "[*] Please insert your username: ");
                String nickname = inputInit.nextLine();


                String pass = "";
                if (Server.getInstance().isConfigurationRequired()) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\n[*] Please insert your password: ");
                    pass = inputInit.nextLine();
                }

                cw = new CliView();

                try {
                    if (network == 1) {
                        SocketClient sc = new SocketClient(serverURL, Integer.parseInt(serverPORT), nickname, pass, cw);
                        cw.addObserver(sc);
                    }

                    if (network == 2) {
                        RMIClient rmiClient = new RMIClient();
                        RMIClientImplementation rmiCI = rmiClient.run(cw, serverURL, nickname, pass);
                        if (rmiCI == null) throw new RemoteException("Connection to RMI server was unsuccessful");
                        else cw.addObserver(rmiCI);
                    }

                    break;
                } catch (AuthenticationErrorException e ) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, e.toString() + "\n");

                } catch (Exception e) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "There was a problem connecting to the server. Please try again.\n");
                    serverPORT = null;
                    serverURL = null;
                    continue;
                }
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
