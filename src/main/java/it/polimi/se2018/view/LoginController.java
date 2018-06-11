package it.polimi.se2018.view;

import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.SocketClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
public class LoginController implements Serializable{

    @FXML private transient Button button;
    @FXML private transient TextField username;
    @FXML private transient TextField password;
    @FXML private transient RadioButton rmi;
    @FXML private transient RadioButton socket;
    @FXML private transient TextField server;
    String methodConnection;

    @FXML
    private void initialize() {
        if (!(Server.getInstance().isConfigurationRequired())) {
            password.setDisable(true);
            button.setDisable(true);
        }
        final ToggleGroup group = new ToggleGroup();
        rmi.setToggleGroup(group);
        rmi.setUserData("RMI");
        socket.setToggleGroup(group);
        socket.setUserData("socket");

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (group.getSelectedToggle() != null) {
                    methodConnection = group.getSelectedToggle().getUserData().toString();
                    button.setDisable(false);
                    System.out.println(group.getSelectedToggle().getUserData().toString());
                }
            }
        });


    }

    @FXML
    private void connectionEvent(){
        String logInUsernme = username.getText();
        String logInPassword = password.getText();
        String serverURL = server.getText();

        SocketClient sc;
        RMIClient rmiClient;
        GuiView gw = new GuiView();

        if (!(Server.getInstance().isConfigurationRequired())) {
            logInPassword = null;
        }

        if(methodConnection.equals("RMI")){
            //RMI CONNECTION
            rmiClient = new RMIClient();
            gw.addObserver(rmiClient.run(gw, serverURL, logInUsernme, logInPassword));
        }else if(methodConnection.equals("socket")){
            //SOCKET CONNECTION
            sc = new SocketClient(serverURL, 9091, logInUsernme, logInPassword, gw);
            gw.addObserver(sc);
        }
        gw.run((Stage)button.getScene().getWindow());
    }
}
