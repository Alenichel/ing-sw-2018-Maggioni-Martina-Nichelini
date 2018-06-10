package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameWindowController implements Serializable {
    @FXML private transient AnchorPane page;
    @FXML private transient Label name0;
    @FXML private transient Label name1;
    @FXML private transient Label name3;
    @FXML private transient Label name2;
    @FXML private transient GridPane windowPattern0;
    @FXML private transient GridPane windowPattern1;
    @FXML private transient GridPane windowPattern2;
    @FXML private transient GridPane windowPattern3;
    @FXML private transient ImageView privateObjective;
    @FXML private transient ImageView objective1;
    @FXML private transient ImageView objective3;
    @FXML private transient ImageView objective2;
    @FXML private transient ImageView mouseOverPublicObjective;
    @FXML private transient Label timerLeft;
    @FXML private transient ImageView dot1;
    @FXML private transient ImageView dot2;
    @FXML private transient ImageView dot3;
    @FXML private transient ImageView dot4;
    @FXML private transient ImageView dot5;
    @FXML private transient ImageView dot6;
    @FXML private transient ImageView clock;
    @FXML private transient ImageView privateObjectiveZoom;
    @FXML private transient Button passTurn;

    private GuiView gw;

    transient ArrayList<Label> labels;
    transient ArrayList<GridPane> gridPanes;

    protected void setup(Game game){
        labels = new ArrayList<>();
        gridPanes = new ArrayList<>();
        labels.add(name0);
        labels.add(name1);
        labels.add(name2);
        labels.add(name3);
        gridPanes.add(windowPattern0);
        gridPanes.add(windowPattern1);
        gridPanes.add(windowPattern2);
        gridPanes.add(windowPattern3);


        passTurn.setOnMouseClicked((MouseEvent e) -> {
            handlePassTurn();
        });
    }

    private void handlePassTurn(){
        gw.passTurn();
    }

    protected void printGameWindow(Game game, Player me, GuiView gw){
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

        if(w.getColorConstraint() != null){
            str = "/constraint/color/"+w.getColorConstraint()+".png";
            //System.out.println("color constraint image path: "+str);
        }
        else if(w.getNumberConstraint() != 0){
            str = "/constraint/number/"+w.getNumberConstraint()+".png";
            //System.out.println("number constraint image path: "+str);
        }
        else if(w.getAssignedDice() != null) {
            str = "/dice/"+w.getAssignedDice().getColor()+"/"+w.getNumberConstraint()+".png";
            //System.out.println("dice path: "+str);
        }else{
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
                                Image image = new Image(path);

                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(50);
                                imageView.setFitWidth(50);

                                Pane pane = (Pane) getNodeByRowColumnIndex(in.getRow(), in.getColumn(), gridPane);
                                pane.getChildren().add(imageView);
                            }
                        }
                    }
                }
            }
            //serve per non riempire le pattern di nessuno
            if (nowPlayer.getNickname().equals("#")) return;
        }

    }


    protected void printCurrentRound(Game game, Player me){
        DropShadow dropShadow = new DropShadow();
        DropShadow dropShadowEnd = new DropShadow();
        dropShadow.setColor(Color.LIGHTBLUE);
        dropShadow.setRadius(30.0);
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
    }




}
