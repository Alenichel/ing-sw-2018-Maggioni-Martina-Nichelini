package it.polimi.se2018.view;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.RequestMessage;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.model.Game;
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

import static it.polimi.se2018.view.LoginController.pstage;

public class GuiView extends View implements Observer {
    private WaitingAreaController waitingAreaController = new WaitingAreaController();

    public void run() {
        Platform.runLater(
                ()->{
                    waitingAreaController.setupWaitingAreaController();
                }
        );
    }

    public void setupLoginView(Stage primaryStage){
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
                String wtu = ((UpdateMessage)message).getWhatToUpdate();

                if(wtu.equals("NewPlayer")){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            waitingAreaController.printGameName(((Game)o).getName());
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION, message.toString());
                        }
                    });
                }

                break;
            default: break;
        }
    }

    private void onGameStarted(GameNames name) {
        waitingAreaController.printGameName(name);
    }
}
