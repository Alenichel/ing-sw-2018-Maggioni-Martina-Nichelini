package it.polimi.se2018.view;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PublicObjectiveCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

import javax.xml.soap.Text;

public class GameWindowController implements Serializable {

    @FXML private transient Label name0;
    @FXML private transient Label name1;
    @FXML private transient Label name3;
    @FXML private transient Label name2;
    @FXML private transient Label favToken;
    @FXML private transient GridPane windowPattern0;
    @FXML private transient GridPane windowPattern1;
    @FXML private transient GridPane windowPattern2;
    @FXML private transient GridPane windowPattern3;
    @FXML private transient ImageView privateObjective;
    @FXML private transient ImageView objective1;
    @FXML private transient ImageView objective3;
    @FXML private transient ImageView objective2;
    @FXML private transient ImageView mouseOverPublicObjective;
    @FXML private transient Label timerLeft;

    protected void print(Game game, Player me){
        printFavourToken(me);
        printPlayerName(game.getPlayers(), me);
        printPrivateObjective(me);
        printPublicObjective(game.getObjectiveCards());
        //printTimerLeft(game.getTimerSecondsLeft());


        objective1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective1.getImage());
            }
        });

        objective1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

        objective2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective2.getImage());
            }
        });

        objective2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

        objective3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective3.getImage());
            }
        });

        objective3.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

    }

    private void printPlayerName(List<Player> ps, Player me){
        ArrayList<Label> labels = new ArrayList<>();
        ArrayList<GridPane> gridPanes = new ArrayList<>();
        labels.add(name0);
        labels.add(name1);
        labels.add(name2);
        labels.add(name3);
        gridPanes.add(windowPattern0);
        gridPanes.add(windowPattern1);
        gridPanes.add(windowPattern2);
        gridPanes.add(windowPattern3);

        int n = 1;

        labels.get(0).setText(me.getNickname());


        for(Player p: ps){
            if(!p.getNickname().equals(me.getNickname())) {
                labels.get(n).setText(p.getNickname());
                n++;
            }
        }
        for(int i = ps.size(); i<4; i++){
            labels.get(i).setVisible(false);
            gridPanes.get(i).setVisible(false);
        }
    }

    private void printFavourToken(Player p){
        favToken.setText(((Integer) p.getActivePatternCard().getNumberOfFavorTokens()).toString());
    }

    private void printPrivateObjective(Player me){
        String url =  "/privateObjective/"+me.getPrivateObjectiveCard().getColor()+".png";

        Image image = new Image(url);
        privateObjective.setImage(image);
    }

    private void printPublicObjective(List<PublicObjectiveCard> ps){
        String partOfPath ="/publicObjective/";
        String endPath = ".png";
        String url1 =  partOfPath+ps.get(0).getName()+endPath;
        Image image1 = new Image(url1);
        String url2 =  partOfPath+ps.get(1).getName()+endPath;
        Image image2 = new Image(url2);
        String url3 =  partOfPath+ps.get(2).getName()+endPath;

        Image image3 = new Image(url3);

        objective1.setImage(image1);
        objective2.setImage(image2);
        objective3.setImage(image3);
    }

    protected void printTimerLeft(int t){
        int min = t/60;
        int sec = t%60;
        timerLeft.setText("min: "+min+"sec: "+sec);
    }




}
