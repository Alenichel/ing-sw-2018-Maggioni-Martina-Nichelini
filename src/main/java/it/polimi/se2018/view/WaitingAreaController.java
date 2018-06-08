package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.network.SocketClient;
import it.polimi.se2018.utils.GameNames;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingAreaController implements Serializable{

    @FXML private transient ListView<?> onlinePlayers;
    @FXML private transient Text gameName;
    @FXML private transient Label waitingTimer;
    @FXML private transient Label nOfPlayers;
    @FXML private transient AnchorPane gameView;
    @FXML private transient Label player1;
    @FXML private transient Label player2;
    @FXML private transient Label player3;
    @FXML private transient Label player4;


    private Scene scene;


    protected void setupWaitingAreaController(Stage primaryStage){
        scene = primaryStage.getScene();
    }


    public void printGameName( GameNames name ){
            gameName.setText(name.toString());
    }

    public void printPlayerCount( int n ){
        nOfPlayers.setText(Integer.toString(n));
    }

    public void printTimer( int n ){
        waitingTimer.setText(Integer.toString(n));
    }

    public void printOnlinePlayers(List<Player> players){
        ArrayList<Label> panes = new ArrayList<Label>();

        panes.add(player1);
        panes.add(player2);
        panes.add(player3);
        panes.add(player4);
        Label appoLabel;

        int n = 0;

        for(Player p : players){
            panes.get(n).setText(p.getNickname());
            n++;
        }
        for(int i = n; i < 4; i++){
            appoLabel = panes.get(i);
            appoLabel.setText("Not already connected");
        }
    }

}

