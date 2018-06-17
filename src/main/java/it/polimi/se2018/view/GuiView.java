package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GuiView extends View implements Observer {
    private transient WaitingAreaController waitingAreaController;
    private transient SelectPatternCardWindowController selectPatternCardWindowController;
    private transient GameWindowController gameWindowController;

    private transient Stage primaryStage;
    private transient Scene sceneWaintingRoom;
    private transient Scene scenePatternCard;
    private transient Scene sceneGame;
    private ArrayList<ToolCard> toolCards;

    public void run(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setupSelectPatternCard();
        setupWaintingArea();
        setupGameWindow();
        printWaintingArea();
    }

    public void setupWaintingArea(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WaitingArea.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setTitle("Wainting area");
            sceneWaintingRoom = new Scene(root);

            waitingAreaController = loader.getController();
        } catch (IOException e) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
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
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
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
        }catch (IOException e){
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
        }

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
        gameWindowController.printGameWindow(game, player, this);
        primaryStage.setScene(sceneGame);
        primaryStage.show();
    }



    @Override
    public void controllerCallback(Message msg) {
        if (msg instanceof ControllerCallbackMessage) {
            if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.MoveAck)) {
                gameWindowController.getControllerCallbackSemaphore().release(2);
            } else if (((ControllerCallbackMessage) msg).getCallbackMessageSubject() != null && ((ControllerCallbackMessage) msg).getCallbackMessageSubject().equals(CallbackMessageSubject.MoveAck)) {
                gameWindowController.getControllerCallbackSemaphore().release(1);
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



    private Object handleUseIO(ToolcardContent tc){
        Object o = new Object();
        switch (tc){
            case DraftedDie:
                o = gameWindowController.draftedSetup();

                break;
        }
        return o;
    }

    protected void useTool(int toolNumber){
        Map<ToolcardContent, Object> htc = new HashMap<>();

        ToolCard t = toolCards.get(toolNumber-1);
        if(t.getContent() != null)
            for(ToolcardContent tc : t.getContent()){
                htc.put(tc, this.handleUseIO(tc));
            }
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
                        else if(wtu.equals(WhatToUpdate.ActivePlayer)){
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

    private void onGameStarted(GameNames name) {
        waitingAreaController.printGameName(name);
    }
}
