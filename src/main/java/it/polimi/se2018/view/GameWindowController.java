package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameWindowController implements Serializable {
    @FXML private AnchorPane page;
    @FXML private Label name0;
    @FXML private Label name1;
    @FXML private Label name3;
    @FXML private Label name2;
    @FXML private GridPane windowPattern0;
    @FXML private GridPane windowPattern1;
    @FXML private GridPane windowPattern2;
    @FXML private GridPane windowPattern3;
    @FXML private ImageView privateObjective;
    @FXML private ImageView objective1;
    @FXML private ImageView objective3;
    @FXML private ImageView objective2;
    @FXML private ImageView mouseOverPublicObjective;
    @FXML private Label timerLeft;
    @FXML private ImageView dot1;
    @FXML private ImageView dot2;
    @FXML private ImageView dot3;
    @FXML private ImageView dot4;
    @FXML private ImageView dot5;
    @FXML private ImageView dot6;
    @FXML private ImageView clock;
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

    private GuiView gw;
    boolean draggable = false;
    transient List<Label> labels;
    transient List<GridPane> gridPanes;
    transient List<Pane> draftedDice;

    private void setup(Game game){
        labels = new ArrayList<>();
        gridPanes = new ArrayList<>();
        draftedDice = new ArrayList<>();
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

        this.setDragDetection();
        this.setTargetEvents();


        passTurn.setDisable(true);
        passTurn.setOnMouseClicked((MouseEvent e) -> {
            handlePassTurn();
        });

    }

    private void setDragDetection(){
        for(Pane p : draftedDice){
            p.setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if(draggable) {
                        Dragboard db = p.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(p.getBackground().getImages().get(0).getImage());
                        db.setContent(content);
                        event.consume();
                    }
                }
            });
        }
    }

    private void setTargetEvents(){
        for(int x = 0; x<= 4; x++)
            for(int y = 0; y<=3; y++){
                Pane pane = (Pane)getNodeByRowColumnIndex(y, x, windowPattern0);
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

                Dragboard db = event.getDragboard();
                boolean success = false;

                BackgroundImage myBI= new BackgroundImage(db.getImage(),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

                target.setBackground(new Background(myBI));
                success = true;

                for(Pane p : draftedDice){
                    if(target.getBackground().getImages().get(0).getImage().equals(p.getBackground().getImages().get(0).getImage())){
                        p.setBackground(null);
                    }
                }

                event.setDropCompleted(success);

                event.consume();
            }
        });
    }

    //------------------------------------------------------------------

    private void handlePassTurn(){
        gw.passTurn();
    }

    protected void printGameWindow(Game game, Player me, GuiView gw) {
        this.gw = gw;
        setup(game);
        printFavourToken(me);
        printPlayerName(game.getPlayers(), me);
        printPrivateObjective(me);
        printPublicObjective(game.getObjectiveCards());
        printPatternCards(game);

        objective1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective1.getImage());
            }
        });
        objective1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

        objective2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective2.getImage());
            }
        });
        objective2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

        objective3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(true);
                mouseOverPublicObjective.setImage(objective3.getImage());
            }
        });
        objective3.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseOverPublicObjective.setVisible(false);
            }
        });

        privateObjective.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                privateObjectiveZoom.setVisible(true);
                privateObjectiveZoom.setImage(privateObjective.getImage());
            }
        });
        privateObjective.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                privateObjectiveZoom.setVisible(false);
            }
        });
    }

    private void printPlayerName(List<Player> ps, Player me){
        int n = 1;

        labels.get(0).setText(me.getNickname());
        labels.get(0).setText(me.getNickname());
        gridPanes.get(0).setId(me.getNickname() + "-windowPattern");

        for(Player p: ps){
            gridPanes.get(n).setId(p.getNickname() + "-windowPattern");
            if(!p.getNickname().equals(me.getNickname())) {
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

    protected void printTimerLeft(int t){
        Image clockImage = new Image("/clock.gif");
        clock.setImage(clockImage);
        int min = t/60;
        int sec = t%60;
        timerLeft.setText("min: "+min+" sec: "+sec);
    }

    protected void printPatternCards(Game game){
        for(GridPane gridPane : gridPanes){
            printPatternCard(gridPane, game);
        }
    }

    protected String toPath(WindowCell w){
        String str="";
        if(w.getAssignedDice() != null) {
            str = "/dice/"+w.getAssignedDice().getColor()+"/"+w.getAssignedDice().getNumber()+".png";
            //System.out.println("dice path: "+str);
        }
        else if(w.getColorConstraint() != null){
            str = "/constraint/color/"+w.getColorConstraint()+".png";
            //System.out.println("color constraint image path: "+str);
        }
        else if(w.getNumberConstraint() != 0){
            str = "/constraint/number/"+w.getNumberConstraint()+".png";
            //System.out.println("number constraint image path: "+str);
        }
        else{
            str ="BLANK";
        }
        return str;
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
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
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    private void printWindowCell(WindowCell in, String path, GridPane gridPane){
        BackgroundImage myBI= new BackgroundImage(new Image(path,55,55,false,true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Pane pane = (Pane) getNodeByRowColumnIndex(in.getRow(), in.getColumn(), gridPane);
        pane.setBackground(new Background(myBI));
    }

    private void printPatternCard(GridPane gridPane, Game game) {
        String playerName = gridPane.getId().split("-")[0];
        Player nowPlayer = new Player("#");
        WindowPatternCard windowPatternCard;

        if (!playerName.equals("nada")) {
            for (Player p : game.getPlayers()) {
                if (p.getNickname().equals(playerName)) {
                    nowPlayer = p;
                    windowPatternCard = nowPlayer.getActivePatternCard();
                    for (WindowCell windowCell[] : windowPatternCard.getGrid()) {
                        for (WindowCell in : windowCell) {
                            String path = toPath(in);
                            if (!path.equals("BLANK")) {
                                printWindowCell(in, path, gridPane);
                            }
                        }
                    }
                }
            }
            //serve per non riempire le pattern di nessuno
            if (nowPlayer.getNickname().equals("#")) return;
        }
    }

    protected void printCurrentRound( Player me){
        DropShadow dropShadow = new DropShadow();
        DropShadow dropShadowEnd = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(60.0);
        dropShadowEnd.setRadius(0);
        int n = 0;

        for(GridPane g : gridPanes){
            if(g.getId().split("-")[0].equals(me.getNickname())){
                gridPanes.get(n).setEffect(dropShadow);
            }else{
                gridPanes.get(n).setEffect(null);
            }
            n++;
        }
        toggleDraggable(me);
    }

    private void toggleDraggable(Player currentPlayer){
        String player = windowPattern0.getId().split("-")[0];

        if(player.equals(currentPlayer.getNickname())){
            draggable = true;
        }else{
            draggable = false;
        }

    }

    protected void printDratfedDice(List<Dice> dices){
        passTurn.setDisable(false);
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
}
