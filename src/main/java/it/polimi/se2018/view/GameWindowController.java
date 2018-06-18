package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWindowController implements Serializable {
    @FXML private AnchorPane page;

    @FXML private Label name0;
    @FXML private Label name1;
    @FXML private Label name3;
    @FXML private Label name2;

    @FXML private Label roundLabel;

    @FXML private GridPane windowPattern0;
    @FXML private GridPane windowPattern1;
    @FXML private GridPane windowPattern2;
    @FXML private GridPane windowPattern3;

    @FXML private ImageView privateObjective;

    @FXML private ImageView objective1;
    @FXML private ImageView objective3;
    @FXML private ImageView objective2;
    @FXML private ImageView mouseOverPublicObjective;
    @FXML private ImageView tool1;
    @FXML private ImageView tool2;
    @FXML private ImageView tool3;

    @FXML private Label timerLeft;

    @FXML private ImageView dot1;
    @FXML private ImageView dot2;
    @FXML private ImageView dot3;
    @FXML private ImageView dot4;
    @FXML private ImageView dot5;
    @FXML private ImageView dot6;

    @FXML private ImageView privateObjectiveZoom;

    @FXML private Button passTurn;

    @FXML private Pane drafted1;
    @FXML private Pane drafted2;
    @FXML private Pane drafted3;
    @FXML private Pane drafted4;
    @FXML private Pane drafted5;
    @FXML private Pane drafted6;
    @FXML private Pane drafted7;
    @FXML private Pane drafted8;
    @FXML private Pane drafted9;

    @FXML protected Pane responeInsert;

    @FXML private Pane winnerPane;
    @FXML private ImageView winnerImage;
    @FXML private Text winnerText;
    @FXML private Button winnerEndGame;
    @FXML private Button winnerAnoterMatch;

    @FXML private Pane roundTrack0;
    @FXML private Pane roundTrack1;
    @FXML private Pane roundTrack2;
    @FXML private Pane roundTrack3;
    @FXML private Pane roundTrack4;
    @FXML private Pane roundTrack5;
    @FXML private Pane roundTrack6;
    @FXML private Pane roundTrack7;
    @FXML private Pane roundTrack8;
    @FXML private Pane roundTrack9;

    @FXML private Button useTool;

    //private static Object o;
    protected GuiView gw;
    protected boolean draggable = false;
    private boolean mouseOver = true;
    private boolean toolInUse = false;
    private List<Label> labels;
    private List<GridPane> gridPanes;
    private List<Pane> draftedDice;
    private List<Pane> roundTrack;
    private List<ImageView> toolCards;
    private List<ImageView> publicObjectives;
    private final GameWindowController thisController = this;

    protected Semaphore controllerCallbackSemaphore;
    protected Semaphore toolcardSemaphore;
    protected Object toolCardDragBoard;

    protected Pane selectedPane;
    protected int column;
    protected int row;

    private void setup(int nOfPlayers){
        labels = new ArrayList<>();
        gridPanes = new ArrayList<>();
        draftedDice = new ArrayList<>();
        roundTrack = new ArrayList<>();
        toolCards = new ArrayList<>();
        publicObjectives = new ArrayList<>();

        publicObjectives.add(objective1);
        publicObjectives.add(objective2);
        publicObjectives.add(objective3);
        toolCards.add(tool1);
        toolCards.add(tool2);
        toolCards.add(tool3);
        labels.add(name0);
        labels.add(name1);
        labels.add(name2);
        labels.add(name3);
        gridPanes.add(windowPattern0);
        gridPanes.add(windowPattern1);
        gridPanes.add(windowPattern2);
        gridPanes.add(windowPattern3);
        draftedDice.add(drafted1);
        draftedDice.add(drafted2);
        draftedDice.add(drafted3);
        draftedDice.add(drafted4);
        draftedDice.add(drafted5);
        draftedDice.add(drafted6);
        draftedDice.add(drafted7);
        draftedDice.add(drafted8);
        draftedDice.add(drafted9);
        roundTrack.add(roundTrack0);
        roundTrack.add(roundTrack1);
        roundTrack.add(roundTrack2);
        roundTrack.add(roundTrack3);
        roundTrack.add(roundTrack4);
        roundTrack.add(roundTrack5);
        roundTrack.add(roundTrack6);
        roundTrack.add(roundTrack7);
        roundTrack.add(roundTrack8);
        roundTrack.add(roundTrack9);


        for(ImageView imageView : publicObjectives){
            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(mouseOver) {
                        mouseOverPublicObjective.setVisible(true);
                        mouseOverPublicObjective.setImage(imageView.getImage());
                    }
                }
            });
            imageView.setOnMouseExited(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    mouseOverPublicObjective.setVisible(false);
                }
            });
        }

        for(ImageView imageView : toolCards){
            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(mouseOver) {
                        mouseOverPublicObjective.setVisible(true);
                        mouseOverPublicObjective.setImage(imageView.getImage());
                    }
                }
            });

            imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mouseOverPublicObjective.setVisible(false);
                }
            });

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    toolcardSemaphore = new Semaphore(0);
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Semaphor initialized");
                    gw.useTool(Integer.parseInt(imageView.getId().substring(imageView.getId().length()-1)));
                }
            });
        }

        useTool.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!toolInUse) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Toolcard button pressed");
                    mouseOver = false;
                    useTool.setText("Abort");
                    passTurn.setDisable(true);
                    toolInUse = true;
                    for (ImageView imageView : toolCards) {
                        imageView.setCursor(Cursor.HAND);
                    }
                }else{
                    useTool.setText("Use tool card");
                    mouseOver = true;
                    toolInUse = false;
                    passTurn.setDisable(false);
                }
            }
        });

        this.setSourceEvents();
        this.setTargetEvents();


        passTurn.setOnMouseClicked((MouseEvent e) -> gw.passTurn());
        String path;

        if(nOfPlayers == 4)
            path = "/backgrounds/4player.png";
        else if(nOfPlayers == 3)
            path = "/backgrounds/3player.png";
        else
            path = "/backgrounds/2player.png";

        BackgroundImage myBI= new BackgroundImage(new Image(path,1275,720,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        page.setBackground(new Background(myBI));
    }

    private void setSourceEvents(){
        for(Pane p : draftedDice){
            this.setOnDragDetection(p);
            this.setOnDragDone(p);
        }
    }

    private void setOnDragDetection(Pane source){
        source.setOnDragDetected( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(draggable) {
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();

                    //put as content last number of the ID
                    content.putString(source.getId().substring(source.getId().length()-1));

                    //put as image the background image of the dragged die
                    content.putImage(source.getBackground().getImages().get(0).getImage());
                    db.setContent(content);
                    event.consume();
                }
            }
        });
    }

    private void setOnDragDone(Pane source){
        source.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() != null) source.setBackground(null);
                event.consume();
            }
        });
    }

    private void setTargetEvents(){
        for(int x = 0; x<= 4; x++)
            for(int y = 0; y<=3; y++){
                Pane pane = (Pane)getNodeByRowColumnIndex(y, x, windowPattern0);
                pane.setId("pane-"+(y+1)+"-"+(x+1));
                this.setOnDragOver(pane);
                this.setOnDragDropped(pane);
            }
    }

    private void setOnDragOver(Pane target){
        target.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                if (event.getGestureSource() != target ) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            }
        });
    }

    private void setOnDragDropped (Pane target){
        target.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(5), (ActionEvent even) -> removeResponse()));
                controllerCallbackSemaphore = new Semaphore(2);
                controllerCallbackSemaphore.acquireUninterruptibly(2);
                int die = Integer.parseInt(event.getDragboard().getString());
                DragTask dragTask = new DragTask(thisController, target, event, timer, die);
                new Thread(dragTask).start();
            }
        });
    }

    public Semaphore getControllerCallbackSemaphore() {
        return controllerCallbackSemaphore;
    }

    protected void ToolcardWindowEffect(){

        for(Node node : gridPanes.get(0).getChildren()){

            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);

            if(c != null && r != null){
                node.setCursor(Cursor.MOVE);
                node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //System.out.println("Clicked on x: " + c + "y: "+ r );
                        column = GridPane.getColumnIndex((Node)event.getSource());
                        row = GridPane.getRowIndex((Node)event.getSource());
                        int[] coordinate = {column, row};
                        toolCardDragBoard = coordinate;
                        toolcardSemaphore.release();
                    }
                });
            }
        }
    }

    protected void ToolcardDraftPoolEffect(){
        for(Node node: draftedDice){
            Pane p = (Pane) node;
            int index = Integer.parseInt(p.getId().substring(p.getId().length()-1));
            node.setCursor(Cursor.MOVE);
            node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    toolCardDragBoard = index-1;
                    toolcardSemaphore.release();
                }
            });
        }
    }

    private void removeResponse(){
        responeInsert.setBackground(null);
    }

    protected void printGameWindow(Game game, Player me, GuiView gw) {
        this.gw = gw;
        mouseOver = true;
        if (game.getActualRound() == 1) setup(game.getPlayersOrder().size());
        printFavourToken(me);
        printRoundLabel(game);
        printPlayerName(game.getPlayersOrder(), me);
        printPrivateObjective(me);
        printPublicObjective(game.getObjectiveCards());
        printToolCards(game.getToolCards());
        printPatternCards(game);
        printDratfedDice(game.getDiceOnTable());
        printCurrentRound(game.getActivePlayer());
        printRoundTrack(game.getRoundTrack(), game.getActualRound());
        togglePassTurn(game, me);
    }

    private void printRoundLabel(Game game){
        roundLabel.setTextFill(Color.WHITE);
        roundLabel.setText("Round: " + String.valueOf(game.getActualRound()));
    }

    protected void printTimerLeft(int t){
        String clk="";
        int min = t/60;
        int sec = t%60;

        if(min != 0)
            if(min > 9)
                clk = clk.concat(Integer.toString(min)+" : ");
            else
                clk = clk.concat("0"+ Integer.toString(min)+" : ");
        else
            clk = clk.concat("00 : ");

        if(sec>9)
            clk = clk.concat(Integer.toString(sec));
        else
            clk = clk.concat("0" + Integer.toString(sec));

        if(t < 10)
            timerLeft.setTextFill(Color.RED);

        else
            timerLeft.setTextFill(Color.BLACK);

        timerLeft.setText(clk);
    }

    private void printPatternCards(Game game){
        for(GridPane gridPane : gridPanes){
            printPatternCard(gridPane, game);
        }
    }

    private void printPatternCard(GridPane gridPane, Game game) {
        String playerName = gridPane.getId().split("-")[0];
        Player nowPlayer = new Player("#");
        WindowPatternCard windowPatternCard;

        if (!playerName.equals("nada")) {
            for (Player p : game.getPlayersOrder()) {
                if (p.getNickname().equals(playerName)) {
                    nowPlayer = p;
                    windowPatternCard = nowPlayer.getActivePatternCard();
                    for (WindowCell[] windowCell : windowPatternCard.getGrid())
                        for (WindowCell in : windowCell) {
                            String path = toPath(in);
                            if (!path.equals("BLANK")) printWindowCell(in, path, gridPane);
                    }
                }
            }
            //serve per non riempire le pattern di nessuno
            //if (nowPlayer.getNickname().equals("#")) return;
        }
    }

    private void printCurrentRound( Player activePlayer){
        int n = 0;

        for(GridPane g : gridPanes){
            if(g.getId().split("-")[0].equals(activePlayer.getNickname())){
                final String cssDefault = "-fx-border-color: red;\n"
                        + "-fx-border-width: 10;\n";
                g.setStyle(cssDefault);


            }else{
                final String cssDefault = "-fx-border-color: black;\n"
                        + "-fx-border-width: 10;\n";
                g.setStyle(cssDefault);
            }
            n++;
        }
        toggleDraggable(activePlayer);
    }

    private void printDratfedDice(List<Dice> dices){
        int n = 0;
        for(Pane pane: draftedDice){
            pane.setBackground(null);
        }
        for(Dice d : dices){
            String path = "/dice/"+d.getColor()+"/"+d.getNumber()+".png";
            BackgroundImage myBI= new BackgroundImage(new Image(path,55,55,false,true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            Pane draftedDie = draftedDice.get(n);
            draftedDie.setCursor(Cursor.OPEN_HAND);
            draftedDie.setDisable(false);
            draftedDie.setBackground(new Background(myBI));
            n++;
        }
    }

    private void printPlayerName(List<Player> ps, Player me){
        labels.get(0).setText(me.getNickname());
        labels.get(0).setText(me.getNickname());
        gridPanes.get(0).setId(me.getNickname() + "-windowPattern");

        int n = 1;
        for(Player p: ps){
            if(!p.getNickname().equals(me.getNickname())) {
                gridPanes.get(n).setId(p.getNickname() + "-windowPattern");
                labels.get(n).setText(p.getNickname());
                n++;
            }
        }
        for(int i = ps.size(); i<4; i++){
            labels.get(i).setVisible(false);
            gridPanes.get(i).setVisible(false);
            gridPanes.get(n).setId("nada");
        }
    }

    private void printFavourToken(Player p){
        String url =  "dot.png";
        Image image = new Image(url);
        int nToken = p.getActivePatternCard().getNumberOfFavorTokens();
        ArrayList<ImageView> iTokens = new ArrayList<>();
        iTokens.add(dot1);
        iTokens.add(dot2);
        iTokens.add(dot3);
        iTokens.add(dot4);
        iTokens.add(dot5);
        iTokens.add(dot6);
        for(int i = 1; i <= nToken; i++){
            iTokens.get(i-1).setImage(image);
        }

    }

    private void printPrivateObjective(Player me){
        String url =  "/privateObjective/"+me.getPrivateObjectiveCard().getColor()+".png";
        Image image = new Image(url);
        privateObjective.setImage(image);
    }

    private void printPublicObjective(List<PublicObjectiveCard> ps){
        String partOfPath ="/publicObjective/";
        String endPath = ".png";
        String url1 =  partOfPath+ps.get(0).getName()+endPath;
        Image image1 = new Image(url1);
        String url2 =  partOfPath+ps.get(1).getName()+endPath;
        Image image2 = new Image(url2);
        String url3 =  partOfPath+ps.get(2).getName()+endPath;
        Image image3 = new Image(url3);

        objective1.setImage(image1);
        objective2.setImage(image2);
        objective3.setImage(image3);
    }

    private void printToolCards(List <ToolCard> ts){
        String partOfPath ="/toolCards/";
        String endPath = ".png";

        String url1 =  partOfPath+ts.get(0).getToolCardName()+endPath;
        Image image1 = new Image(url1);

        String url2 =  partOfPath+ts.get(1).getToolCardName()+endPath;
        Image image2 = new Image(url2);

        String url3 =  partOfPath+ts.get(2).getToolCardName()+endPath;
        Image image3 = new Image(url3);

        tool1.setImage(image1);
        tool2.setImage(image2);
        tool3.setImage(image3);
    }

    private void printWindowCell(WindowCell in, String path, GridPane gridPane){
        Pane pane = (Pane) getNodeByRowColumnIndex(in.getRow(), in.getColumn(), gridPane);
        if(path.equals("BLANK")){
            pane.setBackground(null);
        }else{
            BackgroundImage myBI= new BackgroundImage(new Image(path,55,55,false,true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            pane.setBackground(new Background(myBI));
        }

    }

    protected void printEndGame(Game game, Player client){
        System.out.println("End controller");
        String text = "";
        winnerPane.setVisible(true);
        winnerPane.setDisable(false);

        if (game.getWinner().getNickname().equals(client.getNickname())){
            winnerImage.setImage(new Image("/victory.png"));
        }
        else {
            winnerImage.setImage(new Image("/lose.png"));
            text = text.concat("The winner is: " + game.getWinner().getNickname());
        }

        for (Player p: game.getPlayersOrder()){
            HashMap<String, Integer> scoreMap = p.getScores();
            text = text.concat("\nPlayer: " + p.getNickname() + " (Score " + p.getScore() + ")");

            for (String s: scoreMap.keySet()){
                text = text.concat("\t- " + s + ": " + scoreMap.get(s)+"\n");
            }
        }

        winnerText.setText(text);
    }


    private String toPath(WindowCell w){
        String str;
        if(w.getAssignedDice() != null) {
            str = "/dice/"+w.getAssignedDice().getColor()+"/"+w.getAssignedDice().getNumber()+".png";
        }
        else if(w.getColorConstraint() != null){
            str = "/constraint/color/"+w.getColorConstraint()+".png";
        }
        else if(w.getNumberConstraint() != 0){
            str = "/constraint/number/"+w.getNumberConstraint()+".png";
        }
        else{
            str ="BLANK";
        }
        return str;
    }

    private Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    private void printRoundTrack(RoundTrack rt, int round){
        if(round > 1) {
            Dice d = rt.getRoundTrack().get(round - 2).get(0);

            String path = "/dice/" + d.getColor() + "/" + d.getNumber() + ".png";
            BackgroundImage myBI = new BackgroundImage(new Image(path, 55, 55, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

            roundTrack.get(round - 2).setBackground(new Background(myBI));
        }


    }
    @FXML
    private void quit(){
        Platform.exit();
        System.exit(0);
    }

    private void toggleDraggable(Player currentPlayer){
        String player = windowPattern0.getId().split("-")[0];

        if(player.equals(currentPlayer.getNickname())){
            draggable = true;
            for(Pane pane : draftedDice)
                pane.setCursor(Cursor.OPEN_HAND);
        }else{
            draggable = false;
            for(Pane pane : draftedDice)
                pane.setCursor(Cursor.CLOSED_HAND);
        }


    }

    private void togglePassTurn(Game game, Player me){
        if(game.getActivePlayer().getNickname().equals(me.getNickname())){
            passTurn.setDisable(false);
            useTool.setDisable(false);
        }else{
            passTurn.setDisable(true);
            useTool.setDisable(true);
        }
    }

    protected void increasePopUp(){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                Popup popup = new Popup();

                Text t = new Text("ciaooooooooooo");

                Button increase = new Button("Increase");
                Button decrease = new Button("Decrease");
                Pane pane = new Pane();

                pane.getChildren().addAll(t, increase, decrease);


                popup.getContent().addAll(t, increase, decrease);

                popup.show(gw.primaryStage);

            }
        });
    }
}
