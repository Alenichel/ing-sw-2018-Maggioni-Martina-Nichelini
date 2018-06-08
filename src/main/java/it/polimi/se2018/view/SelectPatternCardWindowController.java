package it.polimi.se2018.view;

import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


public class SelectPatternCardWindowController implements Serializable {
    private Player player;
    private GuiView gw;
    @FXML private transient ImageView window1;
    @FXML private transient ImageView window2;
    @FXML private transient ImageView window3;
    @FXML private transient ImageView window4;


    protected void printPool(Stage primaryStage, Player player, GuiView gw){
        this.gw = gw;
        this.player = player;
        Scene scene = primaryStage.getScene();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(window1);
        imageViews.add(window2);
        imageViews.add(window3);
        imageViews.add(window4);

        ArrayList<Image> images = new ArrayList<>();

        ArrayList<WindowPatternCard> pool = (ArrayList<WindowPatternCard>) player.getWindowPatternCardsPool();

        for(WindowPatternCard w : pool){
            String url = "/windowPatternCardImage/"+w.getName().toString()+".png";
            //System.out.println("url: " + url);
            Image image = new Image(url);
            images.add(image);
        }
        int i = 0;
        for(ImageView imageView : imageViews){
            imageView.setImage(images.get(i));
            i++;
        }

        window1.setOnMouseClicked((MouseEvent e) -> {
            handleClick(1);
        });
        window2.setOnMouseClicked((MouseEvent e) -> {
            handleClick(2);
        });
        window3.setOnMouseClicked((MouseEvent e) -> {
            handleClick(3);
        });
        window4.setOnMouseClicked((MouseEvent e) -> {
            handleClick(4);
        });
    }

    private void handleClick(int n){
        gw.selectedPatternCard(n);
    }

}