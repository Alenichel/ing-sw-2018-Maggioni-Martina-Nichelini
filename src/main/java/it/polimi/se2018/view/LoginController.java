package it.polimi.se2018.view;

import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.RMIClient;
import it.polimi.se2018.network.RMIClientImplementation;
import it.polimi.se2018.network.SocketClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.Serializable;
import java.rmi.RemoteException;
public class LoginController implements Serializable{

    @FXML private Button button;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private RadioButton rmi;
    @FXML private RadioButton socket;
    @FXML private TextField server;
    @FXML private TextField port;

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
                    if (methodConnection.equals("RMI")) port.setText("1099");
                    else port.setText("9091");
                    button.setDisable(false);
                }
            }
        });


    }

    @FXML
    private void connectionEvent(){
        String logInUsernme = username.getText();
        String logInPassword = password.getText();
        String serverURL = server.getText();
        String serverPort = port.getText();

        SocketClient sc;
        RMIClient rmiClient;
        GuiView gw = new GuiView();

        if (!(Server.getInstance().isConfigurationRequired())) {
            logInPassword = null;
        }

        try {
            if (methodConnection.equals("RMI")) {
                //RMI CONNECTION
                rmiClient = new RMIClient();
                RMIClientImplementation rmiCI = rmiClient.run(gw, serverURL, logInUsernme, logInPassword);
                if (rmiCI == null) throw new RemoteException("Connection to RMI server was unsuccessful");
                else gw.addObserver(rmiCI);

            } else if (methodConnection.equals("socket")) {
                //SOCKET CONNECTION
                sc = new SocketClient(serverURL, Integer.parseInt(serverPort), logInUsernme, logInPassword, gw);
                gw.addObserver(sc);
            }

            gw.run((Stage)button.getScene().getWindow());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("There was an error during login\n" + e.toString());
            alert.setContentText("Please close this window and try again.");

            alert.showAndWait();
        }
    }
}
