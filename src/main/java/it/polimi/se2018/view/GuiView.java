package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

import static it.polimi.se2018.enumeration.LoggerPriority.NORMAL;

public class GuiView extends View implements Observer {
    private transient WaitingAreaController waitingAreaController;
    private transient SelectPatternCardWindowController selectPatternCardWindowController;
    protected transient GameWindowController gameWindowController;

    private transient Stage primaryStage;
    private transient Scene sceneWaintingRoom;
    private transient Scene scenePatternCard;
    private transient Scene sceneGame;

    private ArrayList<ToolCard> toolCards;


    private transient Task<Void> toolCardTask;
    protected transient Semaphore toolcardSemaphore;
    protected transient Object toolCardDragBoard;

    public void run(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setupSelectPatternCard();
        setupWaintingArea();
        setupGameWindow();
        printWaintingArea();
    }

    protected void setupWaintingArea(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WaitingArea.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Wainting area");
            sceneWaintingRoom = new Scene(root);

            waitingAreaController = loader.getController();
        } catch (IOException e) {
            //Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void setupGameWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Game Window");
            sceneGame = new Scene(root);

            gameWindowController = loader.getController();
        } catch (IOException e) {
            //Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void setupSelectPatternCard(){
        Parent root;
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/SelectPatternCardWindow.fxml"));
        try {
            root = loader.load();

            primaryStage.setTitle("Select your pattern card!");
            scenePatternCard = new Scene(root);
            selectPatternCardWindowController = loader.getController();
        }catch (IOException e){
            //Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

    }



    protected void printWaintingArea(){
        primaryStage.setScene(sceneWaintingRoom);
        primaryStage.show();
    }

    private void printSelectPatternCard(){
        primaryStage.setScene(scenePatternCard);
        primaryStage.show();
    }

    private void printGameWindow(Game game, Player player){
        gameWindowController.printGameWindow(game, player, this);
        primaryStage.setScene(sceneGame);
        primaryStage.show();
    }



    @Override
    public void controllerCallback(Message msg) {
        if (msg instanceof ControllerCallbackMessage) {
            if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.MoveAck)) {
                gameWindowController.getControllerCallbackSemaphore().release(2);
            } else if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.MoveNack)) {
                gameWindowController.getControllerCallbackSemaphore().release(1);

            } else if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.ToolCardAck)) {
                final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(5), (ActionEvent even) -> gameWindowController.removeResponse()));
                timer.play();
                gameWindowController.printAck("TOOLCARD ACK");

            } else if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.ToolcardNack)) {
                final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(5), (ActionEvent even) -> gameWindowController.removeResponse()));
                timer.play();
                gameWindowController.printNack("NACK: " + msg.getStringMessage());
            }
        }
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


    protected void useTool(int toolNumber){
        ToolCard selectedToolCard = toolCards.get(toolNumber-1);

        if (this.toolCardTask != null && this.toolCardTask.isRunning()) {
            this.toolCardTask.cancel(true);
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Old task marked as CANCELLED.");
        }
        if(selectedToolCard.getToolCardName().equals(ToolCardsName.LensCutter)){
            //If LensCutter ToolCards is in use lock the mouseOver Pane
            gameWindowController.lensCutterInUse = true;
        }
        this.toolCardTask = new ToolCardTask(selectedToolCard, this, toolcardSemaphore);
        new Thread(toolCardTask).start();
    }

    protected void searchAnotherGame(){
        Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "Looking for another game..");
        ConnectionMessage cm = new ConnectionMessage();
        this.setChanged();
        this.notifyObservers(cm);
    }

    @Override
    public void update(Observable o, Object message) {

        if(o instanceof Game){
            for(Player p : ((Game) o).getPlayers())
                if(p.getNickname().equals(client.getNickname()))
                    client = p;

        }

        switch(((Message)message).getMessageType()){

            case "ControllerCallbackMessage":
                this.controllerCallback((Message)message);
                break;

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
                        else if (wtu.equals(WhatToUpdate.TimeLeft)){
                            waitingAreaController.printTimer(((Game)o).getTimerSecondsLeft());
                            gameWindowController.printTimerLeft(((Game)o).getTimerSecondsLeft());
                        }
                        else if (wtu.equals(WhatToUpdate.GameStarted)){
                            toolCards = (ArrayList<ToolCard>)((Game)o).getToolCards();
                            printSelectPatternCard();
                            selectPatternCardWindowController.printPool(primaryStage, getClient(), this);
                        }
                        else if(wtu.equals(WhatToUpdate.ActivePlayer) || wtu.equals(WhatToUpdate.ToolCardUpdate)){
                            printGameWindow((Game)o, client);
                        }
                        else if (wtu.equals(WhatToUpdate.Winner)){
                            gameWindowController.printEndGame((Game)o, client);
                        }
                    }
                );
            break;
            default: break;
        }
    }
}
