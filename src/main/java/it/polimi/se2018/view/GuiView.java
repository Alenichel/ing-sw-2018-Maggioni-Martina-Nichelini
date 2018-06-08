package it.polimi.se2018.view;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.RequestMessage;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.message.WhatToUpdate;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.GameNames;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class GuiView extends View implements Observer {
    private WaitingAreaController waitingAreaController;
    private SelectPatternCardWindowController selectPatternCardWindowController = new SelectPatternCardWindowController();
    private Stage primaryStage;
    private Player activePlayer;
    private Scene scene;

    public GuiView(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void run() {
        Platform.runLater(
            ()->{
                Parent root;
                FXMLLoader loader= new FXMLLoader(getClass().getResource("/WaitingArea.fxml"));
                try {
                    //root = FXMLLoader.load(getClass().getResource("/SelectPatternCardWindow.fxml"));

                    root = loader.load();
                    primaryStage.setTitle("Select your pattern card!");
                    scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                    waitingAreaController = loader.getController();
                }catch (IOException e){ e.printStackTrace(); }
            }
        );
    }

    public void setupLoginView(){
        Platform.runLater(new Runnable() {
              @Override
              public void run() {
                  try {
                      Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

                      primaryStage.setTitle("Welcome!");
                      primaryStage.setScene(new Scene(root, 620, 300));
                      primaryStage.show();

                  }catch (IOException e){e.printStackTrace();}
              }
          });
    }

    @Override
    public void controllerCallback(Message msg){
    }


    @Override
    public void update(Observable o, Object message) {

        switch(((Message)message).getMessageType()){
            case "UpdateMessage":
                WhatToUpdate wtu = ((UpdateMessage)message).getWhatToUpdate();
                Platform.runLater(
                        ()-> {
                            if (wtu.equals(WhatToUpdate.NewPlayer)) {
                                waitingAreaController.printGameName(((Game) o).getName());
                                waitingAreaController.printPlayerCount(((Game)o).getPlayers().size());
                                //waitingAreaController.printOnlinePlayers(((Game)o).getPlayers());
                                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION, message.toString());
                            }
                            if (wtu.equals(WhatToUpdate.TimeLeft)){
                                waitingAreaController.printTimer(((Game)o).getTimerSecondsLeft());
                            }
                            /*if (wtu.equals("GameStarted")){
                                selectPatternCardWindowController.setupWaitingAreaController(primaryStage, activePlayer);
                            }
                            if(wtu.equals("ActivePlayer")){
                                this.activePlayer = ((Game)o).getActivePlayer();
                            }*/
                        });
                break;
            default: break;
        }
    }

    private void onGameStarted(GameNames name) {
        waitingAreaController.printGameName(name);
    }
}
