package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SelectPatternCardWindowController {


    protected void setupWaitingAreaController(Stage primaryStage, Player player){
        Scene scene = primaryStage.getScene();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add((ImageView) scene.lookup("#window1"));
        imageViews.add((ImageView) scene.lookup("#window2"));
        imageViews.add((ImageView) scene.lookup("#window3"));
        imageViews.add((ImageView) scene.lookup("#window4"));

        ArrayList<Image> images = new ArrayList<>();

        ArrayList<WindowPatternCard> pool = (ArrayList<WindowPatternCard>) player.getWindowPatternCardsPool();
        for(WindowPatternCard w : pool){
            images.add(new Image(getClass().getResource(w.getName()).toString()+".png", true));
        }
        int i = 0;
        for(ImageView imageView : imageViews){
            imageView.setImage(images.get(i));
            i++;
        }
    }

}
