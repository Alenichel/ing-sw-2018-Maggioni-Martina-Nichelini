package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.ToolcardContent;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
    @FXML private ImageView soundIcon;

    @FXML private Label timerLeft;

    @FXML private ImageView dot1;
    @FXML private ImageView dot2;
    @FXML private ImageView dot3;
    @FXML private ImageView dot4;
    @FXML private ImageView dot5;
    @FXML private ImageView dot6;
    @FXML private ImageView arrowUp;
    @FXML private ImageView arrowDown;
    @FXML private ImageView draftPoolArrow;

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
    @FXML private Text hint;
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

    @FXML private Pane mouseOverRound;
    @FXML private GridPane gridPaneMouseOver;


    private List<Dice> draftPoolDice;
    protected GuiView gw;
    protected boolean draggable = false;
    private boolean soundOn = false;
    private boolean mouseOver = true;
    private boolean toolInUse = false;
    private boolean setupped = false;
    private List<Label> labels;
    private List<GridPane> gridPanes;
    private List<Pane> draftedDice;
    private List<Pane> roundTrack;
    private List<ImageView> toolCards;
    private List<ImageView> publicObjectives;
    private final GameWindowController thisController = this;
    private RoundTrack gameRoundTrack;

    protected Semaphore controllerCallbackSemaphore;


    protected Pane selectedPane;
    protected int column;
    protected int row;

    private void musicSetup(){
        Media sound = new Media("http://alenichel.eu/sagrada/musichetta.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setVolume(0);
        soundIcon.setOpacity(0.25);

        soundIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(soundOn) {
                    mediaPlayer.setVolume(0);
                    soundIcon.setOpacity(0.25);
                    soundOn = false;
                } else {
                    mediaPlayer.setVolume(1);
                    soundIcon.setOpacity(1);
                    soundOn = true;
                }
            }
        });
    }

    private void setupToolcards(){
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
                    if(toolInUse) {
                        gw.toolcardSemaphore = new Semaphore(0);
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "Semaphor initialized");
                        gw.useTool(Integer.parseInt(imageView.getId().substring(imageView.getId().length() - 1)));
                    }
                }
            });
        }
    }

    private void setupPublicObjectiveCards(){
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
    }

    private void setupRoundTrack(){
        for(Pane p : roundTrack){
            p.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(p.getBackground() != null) {
                        mouseOverRound.getChildren().clear();

                        ArrayList<Dice> roundDice = gameRoundTrack.getTrack().get(Integer.parseInt(p.getId().substring(p.getId().length()-1)));
                        int n = 0;
                        for(Dice d : roundDice){

                            String path = getPath(d);

                            BackgroundImage myBI= new BackgroundImage(new Image(path,60,60,false,true),
                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

                            Pane p = new Pane();
                            p.setBackground(new Background(myBI));
                            p.setPrefWidth(60);
                            p.setPrefHeight(60);
                            p.setLayoutX(n*60);
                            mouseOverRound.getChildren().add(n, p);

                            mouseOverRound.setVisible(true);
                            mouseOverRound.setDisable(false);
                            n++;
                        }
                    }
                }
            });

            p.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!toolInUse){
                        mouseOverRound.setVisible(false);
                    }
                }
            });
        }
    }

    private void setupButtons(){
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
                    hint.setVisible(false);
                    hint.setDisable(true);
                    draftPoolArrow.setDisable(true);
                    draftPoolArrow.setVisible(false);
                    passTurn.setText("Pass Turn");
                    onToolcardEnd();
                }
            }
        });
        passTurn.setOnMouseClicked((MouseEvent e) -> {
            gw.passTurn();
            hint.setVisible(false);
            hint.setDisable(true);
            draftPoolArrow.setDisable(true);
            draftPoolArrow.setVisible(false);
        });
    }

    // DRAG & DROP SETUP
    //------------------------------------------

    private void setTargetEvents(){
        for(int x = 0; x<= 4; x++)
            for(int y = 0; y<=3; y++){
                Pane pane = (Pane)getNodeByRowColumnIndex(y, x, windowPattern0);
                pane.setId("pane-"+(y+1)+"-"+(x+1));
                this.setOnDragOver(pane);
                this.setOnDragDropped(pane);
            }
    }

    private void setSourceEvents(){
        for(Pane p : draftedDice){
            if (p.getBackground() != null)
                this.setOnDragDetection(p);
        }
    }

    private void setOnDragDetection(Pane source){
        source.setOnDragDetected( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /*if(draggable) {*/
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(source.getId().substring(source.getId().length()-1));

                content.putImage(source.getBackground().getImages().get(0).getImage());
                source.setBackground(null);
                db.setContent(content);
                event.consume();
                //}
            }
        });
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

    //------------------------------------------

    private void setup(int nOfPlayers){
        ImageView[] objectiveArray = {objective1, objective2, objective3};
        publicObjectives = new ArrayList<>(Arrays.asList(objectiveArray));

        ImageView[] toolcardsArray = {tool1, tool2, tool3};
        toolCards = new ArrayList<>(Arrays.asList(toolcardsArray));

        Label[] labelsArray = {name0, name1, name2, name3};
        labels = new ArrayList<>(Arrays.asList(labelsArray));

        GridPane[] gridPanesArray = {windowPattern0, windowPattern1, windowPattern2, windowPattern3};
        gridPanes = new ArrayList<>(Arrays.asList(gridPanesArray));

        Pane[] roundTrackArray = {roundTrack0, roundTrack1, roundTrack2, roundTrack3, roundTrack4, roundTrack5, roundTrack6, roundTrack7, roundTrack8, roundTrack9 };
        roundTrack = new ArrayList<>(Arrays.asList(roundTrackArray));

        Pane[] draftedDiceArray = {drafted1, drafted2, drafted3, drafted4, drafted5, drafted6, drafted7, drafted8, drafted9};
        draftedDice = new ArrayList<>(Arrays.asList(draftedDiceArray));

        //this.musicSetup();
        this.setupToolcards();
        this.setupPublicObjectiveCards();
        this.setupRoundTrack();
        this.setupButtons();
        setBackground(nOfPlayers);
        arrowUp.setImage(new Image("green_up_arrow.png"));
        arrowDown.setImage(new Image("red_down_arrow.png"));
        draftPoolArrow.setImage(new Image("draftPoolArrow.png"));
    }

    private String getPath(Dice d){
        return "/dice/"+d.getColor()+"/"+d.getNumber()+".png";
    }

    private void setBackground(int nOfPlayers){
        String path = "/backgrounds/"+nOfPlayers+"player.png";

        BackgroundImage myBI= new BackgroundImage(new Image(path,1275,720,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        page.setBackground(new Background(myBI));
    }

    public Semaphore getControllerCallbackSemaphore() {
        return controllerCallbackSemaphore;
    }

    protected void toolcardWindowEffect(ToolcardContent tc){

        if (tc.equals(ToolcardContent.WindowCellStart))
            hint.setText("SELECT STARTING CELL");
        else if (tc.equals(ToolcardContent.WindowCellEnd))
            hint.setText("SELECT ENDING CELL");

        hint.setDisable(false);
        hint.setVisible(true);

        for(Node node : gridPanes.get(0).getChildren()){

            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);

            if(c != null && r != null){
                node.setCursor(Cursor.MOVE);
                node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        column = GridPane.getColumnIndex((Node)event.getSource());
                        row = GridPane.getRowIndex((Node)event.getSource());
                        int[] coordinate = {row, column};
                        gw.toolCardDragBoard = coordinate;
                        gw.toolcardSemaphore.release();
                        toolcardCleanWindowEffect();
                    }
                });
            }
        }
    }

    private void toolcardCleanWindowEffect(){
        hint.setDisable(true);
        hint.setVisible(false);

        for (Node node: gridPanes.get(0).getChildren()){
            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);
            if(c != null && r != null) {
                ((Pane) node).setCursor(Cursor.DEFAULT);
                ((Pane) node).setOnMouseClicked(null);
            }
        }
    }

    protected void toolcardDraftPoolEffect(){
        for(Node node: draftedDice){
            Pane p = (Pane) node;
            int index = Integer.parseInt(p.getId().substring(p.getId().length()-1));
            node.setCursor(Cursor.MOVE);
            draftPoolArrow.setDisable(false);
            draftPoolArrow.setVisible(true);

            node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    gw.toolCardDragBoard = index-1;
                    gw.toolcardSemaphore.release();
                    draftPoolArrow.setVisible(false);
                    disableToolcardDrafPoolEffect();
                }
            });
        }
    }

    private void disableToolcardDrafPoolEffect(){
        for (Node node: draftedDice) {
            Pane p = (Pane) node;
            p.setOnMouseClicked(null);
            p.setCursor(Cursor.DEFAULT);
        }
    }

    protected void toolcardIncreaseEffect(){
        arrowUp.setDisable(false);
        arrowDown.setDisable(false);
        arrowUp.setVisible(true);
        arrowDown.setVisible(true);

        arrowUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gw.toolCardDragBoard = true;
                gw.toolcardSemaphore.release();
                toolcardCleanIncreaseEffect();
            }
        });

        arrowDown.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gw.toolCardDragBoard = false;
                gw.toolcardSemaphore.release();
                toolcardCleanIncreaseEffect();
            }
        });
    }

    private void toolcardCleanIncreaseEffect(){
        arrowUp.setVisible(false);
        arrowDown.setVisible(false);
        arrowUp.setOnMouseClicked(null);
        arrowDown.setOnMouseClicked(null);
    }

    protected void onToolcardEnd(){
        useTool.setText("Use ToolCard");
        mouseOver = true;
        toolInUse = false;
        passTurn.setDisable(false);
        hint.setVisible(false);
        hint.setDisable(true);
        draftPoolArrow.setDisable(true);
        draftPoolArrow.setVisible(false);
        passTurn.setText("Pass Turn");
    }

    private void removeResponse(){
        responeInsert.setBackground(null);
    }

    private String toPath(WindowCell w){
        String str;
        if(w.getAssignedDice() != null)
            str = "/dice/"+w.getAssignedDice().getColor()+"/"+w.getAssignedDice().getNumber()+".png";

        else if(w.getColorConstraint() != null)
            str = "/constraint/color/"+w.getColorConstraint()+".png";

        else if(w.getNumberConstraint() != 0)
            str = "/constraint/number/"+w.getNumberConstraint()+".png";

        else str ="BLANK";

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


    //PRINTING SECTION
    //------------------------------------------


    /**
     * This method is called each turn in order to print all windows.
     * @param game
     * @param me
     * @param gw
     */
    protected void printGameWindow(Game game, Player me, GuiView gw) {
        this.gw = gw;
        mouseOver = true;
        if (!this.setupped) {
            setup(game.getPlayersOrder().size());
            this.setupped = true;
        }
        printFavourToken(me);
        printRoundLabel(game);
        printPlayerName(game.getPlayersOrder(), me);
        printPrivateObjective(me);
        printPublicObjective(game.getObjectiveCards());
        printToolCards(game.getToolCards());
        printPatternCards(game);
        printDratfedDice(game.getDiceOnTable());
        printCurrentRound(game.getActivePlayer());
        togglePassTurn(game, me);
        toggleDraggable(game.getActivePlayer());
        this.setSourceEvents();
        this.setTargetEvents();
    }

    private void printRoundLabel(Game game){
        roundLabel.setTextFill(Color.WHITE);
        roundLabel.setText("Round: " + game.getActualRound());
    }

    protected void printTimerLeft(int t){
        String clk="";
        int min = t/60;
        int sec = t%60;

        if(min != 0)
            if(min > 9) clk = clk.concat(Integer.toString(min)+" : ");

            else clk = clk.concat("0"+ Integer.toString(min)+" : ");

        else clk = clk.concat("00 : ");

        if(sec>9) clk = clk.concat(Integer.toString(sec));

        else clk = clk.concat("0" + Integer.toString(sec));

        if(t < 10) timerLeft.setTextFill(Color.RED);

        else timerLeft.setTextFill(Color.BLACK);

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
                            printWindowCell(in, path, gridPane);
                        }
                }
            }
        }
    }

    private void printCurrentRound( Player activePlayer){
        int n = 0;

        for(GridPane g : gridPanes){
            if(g.getId().split("-")[0].equals(activePlayer.getNickname())){
                final String cssDefault = "-fx-border-color: red;\n" + "-fx-border-width: 10;\n";
                g.setStyle(cssDefault);
            }else{
                final String cssDefault = "-fx-border-color: black;\n" + "-fx-border-width: 10;\n";
                g.setStyle(cssDefault);
            }
            n++;
        }
    }

    private void printDratfedDice(List<Dice> dice){
        int n = 0;
        this.draftPoolDice = dice;
        for(Pane pane: draftedDice){
            pane.setBackground(null);
        }
        for(Dice d : dice){
            String path = getPath(d);
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
        String url = "dot.png";
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

    private void printRoundTrack(RoundTrack rt, int round){
        gameRoundTrack = rt;
        if(round > 1) {
            Dice d = rt.getTrack().get(round - 2).get(0);
            String path = getPath(d);

            BackgroundImage myBI = new BackgroundImage(new Image(path, 53, 53, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);


            roundTrack.get(round - 2).setBackground(new Background(myBI));
            roundTrack.get(round - 2).setCursor(Cursor.HAND);
        }
    }

    protected void printAck(){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                BackgroundImage tick= new BackgroundImage(new Image("/tick.png",55,55,false,true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                responeInsert.setBackground(new Background(tick));
            }
        });
    }

    protected void printNack() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BackgroundImage tick = new BackgroundImage(new Image("/x.png", 55, 55, false, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                responeInsert.setBackground(new Background(tick));
            }
        });
    }

    //------------------------------------------


    private void quit(){
        Platform.exit();
        System.exit(0);
    }


    // TOGGLE SECTION
    //------------------------------------------

    private void toggleDraggable(Player currentPlayer){
        String player = windowPattern0.getId().split("-")[0];

        if(player.equals(currentPlayer.getNickname())){
            draggable = true;
            System.out.println("draggable");
            for(Pane pane : draftedDice)
                pane.setCursor(Cursor.OPEN_HAND);
        } else{
            draggable = false;
            System.out.println("NOT draggable");
            for(Pane pane : draftedDice)
                pane.setCursor(Cursor.CLOSED_HAND);
        }
    }

    private void togglePassTurn(Game game, Player me){
        passTurn.setText("Pass Turn");

        arrowDown.setDisable(true);
        arrowDown.setVisible(false);
        arrowUp.setDisable(true);
        arrowUp.setVisible(false);
        hint.setDisable(true);
        hint.setVisible(false);

        onToolcardEnd();

        if(game.getActivePlayer().getNickname().equals(me.getNickname())){
            passTurn.setDisable(false);
            useTool.setDisable(false);
        }else{
            passTurn.setDisable(true);
            useTool.setDisable(true);
        }
    }

    // GETTER
    //------------------------------------------

    public List<Dice> getDraftPoolDice() {
        return draftPoolDice;
    }

    public List<Pane> getDraftedDice() {
        return draftedDice;
    }
}
