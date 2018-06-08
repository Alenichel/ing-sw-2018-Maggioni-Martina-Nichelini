package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ErrorMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.objective.*;
import it.polimi.se2018.strategy.toolcard.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;

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
    private int selectedPatterCards = 0;

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
        ArrayList<PublicObjectiveCard> selectedObject = new ArrayList<>();

        final int[] ints = new Random().ints(0, ObjectiveCardsName.values().length).distinct().limit(3).toArray();
        for (int i : ints)
            selectedObject.add(new PublicObjectiveCard(ObjectiveCardsName.values()[i]));
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


    private void onPatternCardSelection(int cardIndex, Player p) throws GameException{

        if (p.getActivePatternCard() == null) {
            p.assignPatternCard(p.getWindowPatternCardsPool().get(cardIndex));
            p.setWindowPatternCardsPool(null);
            p.getActivePatternCard().setPlayer(p);

            selectedPatterCards++;
            if (selectedPatterCards == associatedGame.getPlayers().size())
                associatedGame.getAssociatedGameController().onInitializationComplete();
        }

        else {
            throw new GameException("AlreadyChosenPatternCard");
        }
    }

    private void handleSelectionMessage(Observable observable, SelectionMessage message){
        switch (message.getSelected()){

            case "PatternCard":
                try {
                    onPatternCardSelection((int)message.getChosenItem(), ((View)observable).getClient());
                }
                catch ( GameException e){
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("You have already selected a pattern card", LoggerPriority.ERROR);
                    ((VirtualView)observable).controllerCallback(ccm);
                }
                catch (IndexOutOfBoundsException e) {
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("You have inserted an out of range value", LoggerPriority.ERROR);
                    ((VirtualView)observable).controllerCallback(ccm);
                }
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

