package it.polimi.se2018.view;

import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.SocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
public class LoginController implements Serializable{

    @FXML private transient Button button;
    @FXML private transient TextField username;
    @FXML private transient TextField password;



    @FXML
    private void initialize() {
        if (!(Server.getInstance().isConfigurationRequired())) {
            password.setDisable(true);
        }
    }

    @FXML
    private void connectionEvent(){
        String logInUsernme = username.getText();
        String logInPassword = password.getText();

        GuiView gw = new GuiView();

        if (!(Server.getInstance().isConfigurationRequired())) {
            logInPassword = null;
        }

        //String methodConnection = toggleConnection.getSelectedToggle().getUserData().toString();
        /*if(methodConnection.equals("rmi")){
            //RMI CONNECTION
            RMIClient rmiClient = new RMIClient();
            gw.addObserver(rmiClient.run(gw, logInUsernme, logInPassword));
        }else {
            //SOCKET CONNECTION
            SocketClient sc = new SocketClient("localhost", 9091, logInUsernme, logInPassword, gw);
            gw.addObserver(sc);
        }*/
        SocketClient sc = new SocketClient("localhost", 9091, logInUsernme, logInPassword, gw);
        gw.addObserver(sc);
        gw.run((Stage)button.getScene().getWindow());
    }
}
