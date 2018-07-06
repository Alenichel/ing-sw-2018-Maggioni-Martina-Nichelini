package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


public class SelectPatternCardWindowController implements Serializable {
    private GuiView gw;
    @FXML private transient ImageView window1;
    @FXML private transient ImageView window2;
    @FXML private transient ImageView window3;
    @FXML private transient ImageView window4;
    @FXML protected transient Pane hadledPane;
    @FXML protected transient Button loadPatternCard;

    private Desktop desktop = Desktop.getDesktop();
    final FileChooser fileChooser = new FileChooser();

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

        setButtonAction();

    }

    private void setButtonAction() {
        loadPatternCard.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                            openFile();
                    }
                });
    }

    private void openFile() { ;
        File file = null;
        file = fileChooser.showOpenDialog(gw.getPrimaryStage());
        String fileName = file.getPath().substring(1);
        try (InputStream xmlResource = new FileInputStream(file);) {
            do {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(xmlResource);
                WindowPatternCard loaded = new WindowPatternCard(fileName, doc );
                System.out.println(loaded);
                hadledPane.setVisible(true);
                handleClick(loaded);
                this.loadPatternCard.setDisable(true);
            }
            while (!(file.getName().matches(".*[a-zA-Z0-9]+.*" + ".xml")));
        }
        catch (NullPointerException | ParserConfigurationException | IOException | SAXException e) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Loading error. Closing..");
        }
    }


    private void handleClick(Object n){
        gw.selectedPatternCard(n);
    }
}


