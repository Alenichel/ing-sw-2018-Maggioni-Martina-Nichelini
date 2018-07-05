package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;


public class SelectPatternCardWindowController implements Serializable {
    private GuiView gw;
    @FXML private transient ImageView window1;
    @FXML private transient ImageView window2;
    @FXML private transient ImageView window3;
    @FXML private transient ImageView window4;
    @FXML protected transient Pane hadledPane;


    protected void printPool(Player player, GuiView gw){
        this.gw = gw;
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(window1);
        imageViews.add(window2);
        imageViews.add(window3);
        imageViews.add(window4);

        hadledPane.setVisible(false);

        ArrayList<Image> images = new ArrayList<>();

        ArrayList<WindowPatternCard> pool = (ArrayList<WindowPatternCard>) player.getWindowPatternCardsPool();

        for(WindowPatternCard w : pool){
            String url = "/windowPatternCardImage/"+w.getName()+".png";
            Image image = new Image(url);
            images.add(image);
        }
        int i = 0;
        for(ImageView imageView : imageViews){
            imageView.setImage(images.get(i));
            i++;
        }

        window1.setOnMouseClicked((MouseEvent e) -> {
            hadledPane.setVisible(true);
            handleClick(0);
        });
        window2.setOnMouseClicked((MouseEvent e) -> {
            hadledPane.setVisible(true);
            handleClick(1);
        });
        window3.setOnMouseClicked((MouseEvent e) -> {
            hadledPane.setVisible(true);
            handleClick(2);
        });
        window4.setOnMouseClicked((MouseEvent e) -> {
            hadledPane.setVisible(true);
            handleClick(3);
        });


    }

    private void handleClick(int n){
        gw.selectedPatternCard(n);
    }
}
