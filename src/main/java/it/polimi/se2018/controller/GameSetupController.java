package it.polimi.se2018.controller;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.objective.*;
import it.polimi.se2018.strategy.toolcard.*;
import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import static it.polimi.se2018.utils.ObjectiveCardsName.LightShades;
import static it.polimi.se2018.utils.ObjectiveCardsName.RowColorVariety;

/**
 * This class handles the first phases of the game in which each players is assigned with all cards and all game elements
 * are initialized.
 */
public class GameSetupController implements Serializable {

    private Game associatedGame;

    public GameSetupController(Game game){
        this.associatedGame = game;
    }

    /**
     * This method initialize all windows pattern cards, before asking player which card he wants to use during this game
     */
    private void initializePatternCard(){
        for(WindowPatternCardsName w : WindowPatternCardsName.values()){
            this.associatedGame.getPatternCards().add(new WindowPatternCard(w));
        }
    }

    private void assignWindowsPatternCardsPool(){
        ArrayList<Player> players = new ArrayList<Player>(associatedGame.getPlayers());
        Random random = new Random();
        ArrayList<WindowPatternCard> genericWindowPatternCards= new ArrayList<WindowPatternCard>(associatedGame.getPatternCards());

        for(Player p : players){
            ArrayList<WindowPatternCard> patternCardsPool = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                int n = random.nextInt(genericWindowPatternCards.size());
                patternCardsPool.add(genericWindowPatternCards.get(n));
                genericWindowPatternCards.remove(n);
            }
            p.setWindowPatternCardsPool(patternCardsPool);
        }
    }

    /**
     * This method initialize three random toolcards
     */
    private void initializeToolCards(){
        Random rand = new Random();
        int n;
        ArrayList<String> toolName = new ArrayList<>();
        ArrayList<ToolCard> selectedToolCards = new ArrayList<ToolCard>();

        for(ToolCardsName t : ToolCardsName.values()){
            toolName.add(t.toString());
        }
        for(int i = 0; i<3; i++){
            n = rand.nextInt(toolName.size());
            selectedToolCards.add(new ToolCard(nameToObjectTool(toolName.get(n))));
            toolName.remove(n);
        }
        this.associatedGame.setToolCards(selectedToolCards);
    }

    /**
     * This method initialize all dice that will be used during the game.
     * According to Sagrada's rules, 15 dice for each color are initialized and they are put on the diceBag.
     */
    private void diceInitializer(){
        for (DiceColor dc : DiceColor.values()){
            for (int i = 0; i < 18; i++){
                Dice newDice = new Dice(dc.name());
                newDice.setLocation(DiceLocation.BAG);
                this.associatedGame.getDiceBag().add(newDice);
            }
        }
    }

    private void initializePublicObject(){
        Random rand = new Random();
        int n;
        ArrayList<String> objectiveName = new ArrayList<>();
        ArrayList<PublicObjectiveCard> selectedObject = new ArrayList<PublicObjectiveCard>();

        for(ObjectiveCardsName po : ObjectiveCardsName.values()){
            objectiveName.add(po.toString());
        }
        for(int i = 0; i<3; i++){
            n = rand.nextInt(objectiveName.size());
            selectedObject.add(new PublicObjectiveCard(nameToObjectObjective(objectiveName.get(n))));
            objectiveName.remove(n);
        }
        this.associatedGame.setObjectiveCards(selectedObject);
    }

    private void initializePrivateObjectiveCards(){
        Random rand = new Random();
        int n;
        ArrayList<DiceColor> colorName = new ArrayList<>();
        for(DiceColor d : DiceColor.values()){
            colorName.add(d);
        }
        for(Player p : this.associatedGame.getPlayers()){
            n = rand.nextInt(this.associatedGame.getPlayers().size());
            p.assignObjectiveCard(new PrivateObjectiveCard(colorName.get(n)));
            colorName.remove(n);
        }
    }

    private ToolCardEffectStrategy nameToObjectTool(String name){
        switch (name){
            case "GrozingPliers" : return new GrozingPliers();
            case "EnglomiseBrush" : return new EnglomiseBrush();
            case "CopperFoilBurnisher" : return new CopperFoilBurnisher();
            case "Lathekin" : return new Lathekin();
            case "LensCutter" : return new LensCutter();
            case "FluxBrush" : return new FluxBrush();
            case "GlazingHammer" : return new GlazingHammer();
            case "RunningPliers" : return new RunningPliers();
            //case "CorkBackedStraightedge" : return new CorkBackedStraightedge();
            case "CorkBackedStraightedge" : return new GrindingStone();
            case "GrindingStone" : return new GrindingStone();
            case "FluxRemover" : return new FluxRemover();
            case "TapWheel" : return new TapWheel();
            default :
                System.out.println(name);
                throw new IllegalArgumentException();

        }
    }

    private ScorePointStrategy nameToObjectObjective(String name){
        switch (name){
            case "RowColorVariety" : return new RowVariety(VarietyType.COLOR);
            case "ColumnColorVariety" : return new ColumnVariety(VarietyType.COLOR);
            case "RowShadeVariety" : return new RowVariety(VarietyType.SHADE);
            case "ColumnShadeVariety" : return new ColumnVariety(VarietyType.COLOR);
            case "LightShades" : return new Shades("light");
            case "MediumShades" : return new Shades("medium");
            case "DarkShades" : return new Shades("dark");
            case "ShadeVariety" : return new ShadeVariety();
            case "ColorDiagonals" : return new ColorDiagonals();
            case "ColorVariety" : return new ColorVariety();
            default :
                throw new IllegalArgumentException();
        }
    }

    private void onPatternCardSelection(int cardIndex, String playerNickName){
        for (Player p: associatedGame.getPlayers()){
            if (p.getNickname().equals(playerNickName)){
                p.assignPatternCard(p.getWindowPatternCardsPool().get(cardIndex));
                p.setWindowPatternCardsPool(null);
                p.setNumberOfFavorTokens(p.getActivePatternCard().getNumberOfFavorTokens());
                p.getActivePatternCard().setPlayer(p);
            }
        }
    }

    private void handleSelectionMessage(Observable observable, SelectionMessage message){
        switch (message.getSelected()){

            case "PatternCard":
                onPatternCardSelection((int)message.getChosenItem(), message.getPlayer().getNickname() );
                break;

            default: break;
        }
    }


    public void initialize(){
        this.diceInitializer();
        this.initializePatternCard();
        this.initializeToolCards();
        this.initializePublicObject();
        this.initializePrivateObjectiveCards();
        this.assignWindowsPatternCardsPool();
    }

    public void update(Observable observable, Object message){
        Message msg = (Message)message;
        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":GAME_SETUP_CONTROLLER: Handling -> " + msg.getMessageType());

        switch (msg.getMessageType()){

            case "SelectionMessage":
                handleSelectionMessage(observable, (SelectionMessage) message);
                break;
            default: break;
        }
    }
}

