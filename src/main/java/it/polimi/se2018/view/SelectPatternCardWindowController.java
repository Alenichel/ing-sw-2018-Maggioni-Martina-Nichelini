package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SelectPatternCardWindowController {
    
    protected void printPool(Stage primaryStage, Player player){
        Scene scene = primaryStage.getScene();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add((ImageView) scene.lookup("#window1"));
        imageViews.add((ImageView) scene.lookup("#window2"));
        imageViews.add((ImageView) scene.lookup("#window3"));
        imageViews.add((ImageView) scene.lookup("#window4"));

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
    }



}
