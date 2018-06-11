package it.polimi.se2018.view;

import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
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
    private transient WaitingAreaController waitingAreaController;
    private transient SelectPatternCardWindowController selectPatternCardWindowController;
    private transient GameWindowController gameWindowController;

    private transient Stage primaryStage;
    private transient Scene sceneWaintingRoom;
    private transient Scene scenePatternCard;
    private transient Scene sceneGame;

    public void run(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setupSelectPatternCard();
        setupWaintingArea();
        setupGameWindow();
        printWaintingArea();
    }

    public void setupWaintingArea(){
        //Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WaitingArea.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Wainting area");
            sceneWaintingRoom = new Scene(root);

            waitingAreaController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupGameWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Game Window");
            sceneGame = new Scene(root);

            gameWindowController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setupSelectPatternCard(){
        Parent root;
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/SelectPatternCardWindow.fxml"));
        try {
            root = loader.load();

            primaryStage.setTitle("Select your pattern card!");
            scenePatternCard = new Scene(root);
            selectPatternCardWindowController = loader.getController();
        }catch (IOException e){e.printStackTrace();}

    }

    public void printWaintingArea(){
        primaryStage.setScene(sceneWaintingRoom);
        primaryStage.show();
    }


    public void printSelectPatternCard(){
        primaryStage.setScene(scenePatternCard);
        primaryStage.show();
    }

    public void printGameWindow(Game game, Player player){
        primaryStage.setScene(sceneGame);
        primaryStage.show();
        gameWindowController.printGameWindow(game, player, this);
        gameWindowController.printPatternCards(game);
        gameWindowController.printDratfedDice(game.getDiceOnTable());
        gameWindowController.printCurrentRound(game.getActivePlayer());
    }

    @Override
    public void controllerCallback(Message msg){
    }

    protected void selectedPatternCard(int n){
        SelectionMessage sm = new SelectionMessage(n, this.client,"PatternCard");
        this.setChanged();
        this.notifyObservers(sm);
    }

    protected void passTurn(){
        this.setChanged();
        this.notifyObservers(new UpdateMessage(WhatToUpdate.Pass));
    }

    protected void placeDice(int n, int x, int y){
        MoveDiceMessage mdm = new MoveDiceMessage(n, x-1, y-1);
        this.setChanged();
        this.notifyObservers(mdm);

    }

    @Override
    public void update(Observable o, Object message) {

        if(o instanceof Game){
            for(Player p : ((Game) o).getPlayers())
                if(p.getNickname().equals(client.getNickname()))
                    client = p;
        }

        switch(((Message)message).getMessageType()){
            case "UpdateMessage":
                WhatToUpdate wtu = ((UpdateMessage)message).getWhatToUpdate();
                Platform.runLater(
                    ()-> {
                        if (wtu.equals(WhatToUpdate.NewPlayer)) {
                            waitingAreaController.printGameName(((Game) o).getName());
                            waitingAreaController.printPlayerCount(((Game)o).getPlayers().size());
                            waitingAreaController.printOnlinePlayers(((Game)o).getPlayers());
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION, message.toString());
                        }
                        if (wtu.equals(WhatToUpdate.TimeLeft)){
                            waitingAreaController.printTimer(((Game)o).getTimerSecondsLeft());
                            gameWindowController.printTimerLeft(((Game)o).getTimerSecondsLeft());
                        }
                        if (wtu.equals(WhatToUpdate.GameStarted)){
                            printSelectPatternCard();
                            selectPatternCardWindowController.printPool(primaryStage, getClient(), this);
                        }
                        if(wtu.equals(WhatToUpdate.ActivePlayer)){
                            printGameWindow((Game)o, client);

                        }
                    }
                );
            break;
            default: break;
        }
    }

    private void onGameStarted(GameNames name) {
        waitingAreaController.printGameName(name);
    }
}
