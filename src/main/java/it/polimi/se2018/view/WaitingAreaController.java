package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.GameNames;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.se2018.view.LoginController.pstage;

public class WaitingAreaController implements Serializable {

    @FXML private ListView<?> onlinePlayers;

    @FXML private Label gameName;

    @FXML private Label waitingTimer;

    @FXML private Label nOfPlayers;

    @FXML private AnchorPane gameView;

    @FXML
    private void initialize() {
        gameName.setText("pippo");
    }

    protected void setupWaitingAreaController(){

        Platform.runLater(
                ()-> {
                    try {
                        URL url = new File("src/main/resources/WaitingArea.fxml").toURL();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(url);
                        gameView = loader.load();
                        pstage.setScene(new Scene(gameView));
                        pstage.setTitle("Waiting area");
                        pstage.show();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    protected void printPlayers(List<Player> players){
        nOfPlayers.setText((Integer.toString(players.size())));
    }

    protected void printGameName(GameNames name){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameName.setText(name.toString());
            }
        });
    }

    @FXML
    protected void printTimer(int timer){
        waitingTimer.setText(Integer.toString(timer));
    }
}
