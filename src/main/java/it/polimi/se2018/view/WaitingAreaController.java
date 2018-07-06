package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.enumeration.GameNames;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for waiting area controller
 */
public class WaitingAreaController implements Serializable{

    @FXML private transient ListView<?> onlinePlayers;
    @FXML private transient Text gameName;
    @FXML private transient Label waitingTimer;
    @FXML private transient Label nOfPlayers;
    @FXML private transient AnchorPane gameView;
    @FXML private transient MenuItem quitItem;
    @FXML private transient Label player1;
    @FXML private transient Label player2;
    @FXML private transient Label player3;
    @FXML private transient Label player4;

    /**
     * This method prints game name
     * @param name game name
     */
    public void printGameName( GameNames name ){
            gameName.setText(name.toString());
    }

    /**
     * This method prints player count
     * @param n number of players
     */
    public void printPlayerCount( int n ){
        nOfPlayers.setText(Integer.toString(n));
    }

    /**
     * This method prints the timer
     * @param n seconds
     */
    public void printTimer( int n ){
        waitingTimer.setText(Integer.toString(n));
    }

    /**
     * This method prints online players
     * @param players list of online players
     */
    public void printOnlinePlayers(List<Player> players){
        ArrayList<Label> panes = new ArrayList<>();

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

