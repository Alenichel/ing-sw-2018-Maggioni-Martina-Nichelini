package it.polimi.se2018.controller;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * This class handles the first phases of the game in which each player is assigned with all cards and all game elements
 * are initialized
 */
public class GameSetupController implements Serializable {

    private Game associatedGame;
    private int selectedPatterCards = 0;
    private transient Random rand = new Random();

    public GameSetupController(Game game){
        this.associatedGame = game;
    }

    /**
     * This method initializes all the window pattern cards before asking the player which card he wants to use during
     * this game
     */
    private void initializePatternCard(){
        for(WindowPatternCardsName w : WindowPatternCardsName.values()){
            this.associatedGame.getPatternCards().add(new WindowPatternCard(w));
        }
    }

    /**
     * This method assigns to each player the pool of pattern cards that will be presented to him.
     */
    private void assignWindowsPatternCardsPool(){

        List<WindowPatternCard> genericWindowPatternCards= new ArrayList<>(associatedGame.getPatternCards());

        for(Player p : this.associatedGame.getPlayers()){
            List<WindowPatternCard> patternCardsPool = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                int n = this.rand.nextInt(genericWindowPatternCards.size());
                patternCardsPool.add(genericWindowPatternCards.get(n));
                genericWindowPatternCards.remove(n);
            }
            p.setWindowPatternCardsPool(patternCardsPool);
        }
    }

    /**
     * This method initializes three random tool cards.
     */
    private void initializeToolCards(){

        List<ToolCard> selectedToolcards= new ArrayList<>();

        final int[] ints = this.rand.ints(0, ToolCardsName.values().length).distinct().limit(3).toArray();
        for (int i : ints)
            selectedToolcards.add(new ToolCard(ToolCardsName.values()[i]));
        this.associatedGame.setToolCards(selectedToolcards);

    }

    /**
     * This method initializes all dice that will be used during the game
     * According to Sagrada's rules, 15 dice for each color are initialized and they are put in the dice bag
     */
    private void diceInitializer(){
        List<Dice> diceBag = this.associatedGame.getDiceBag();
        for (DiceColor dc : DiceColor.values()){
            for (int i = 0; i < 18; i++){
                Dice newDice = new Dice(dc);
                newDice.setLocation(DiceLocation.BAG);
                diceBag.add(newDice);
            }
        }
        this.associatedGame.setDieForSwitch();
    }

    /**
     * This method initializes three random public objective cards
     */
    private void initializePublicObject(){
        List<PublicObjectiveCard> selectedObject = new ArrayList<>();

        final int[] ints = this.rand.ints(0, ObjectiveCardsName.values().length).distinct().limit(3).toArray();
        for (int i : ints)
            selectedObject.add(new PublicObjectiveCard(ObjectiveCardsName.values()[i]));
        this.associatedGame.setObjectiveCards(selectedObject);
    }

    /**
     * This method initializes a private objective card for each player
     */
    private void initializePrivateObjectiveCards(){

        final int[] ints = this.rand.ints(0, DiceColor.values().length).distinct().limit(this.associatedGame.getPlayers().size()).toArray();
        for (int i = 0; i < this.associatedGame.getPlayers().size(); i++){
            this.associatedGame.getPlayers().get(i).assignObjectiveCard(new PrivateObjectiveCard(DiceColor.values()[ints[i]]));
        }
    }

    /**
     * This method is called later in handleSelectionMessage
     * @param cardIndex of the window pattern card
     * @param p player who chose the pattern card
     * @throws GameException
     */
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

    /**
     * This method handles selection message
     */
    private void handleSelectionMessage(Observable observable, SelectionMessage message){
        switch (message.getSelected()){

            case "PatternCard":
                try {
                    onPatternCardSelection((int)message.getChosenItem(), ((View)observable).getClient());
                }
                catch (GameException e){
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("You have already selected a pattern card", LoggerPriority.ERROR);
                    ((VirtualView)observable).controllerCallback(ccm);
                }
                catch (IndexOutOfBoundsException e) {
                    ControllerCallbackMessage ccm = new ControllerCallbackMessage("You have inserted an out of range value", LoggerPriority.ERROR);
                    ((VirtualView)observable).controllerCallback(ccm);
                    Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
                }
                break;

            default: break;
        }
    }

    /**
     * THis method initializes all the elements needed
     */
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
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, ":GAME_SETUP_CONTROLLER: Handling -> " + msg.getMessageType());

        switch (msg.getMessageType()){

            case "SelectionMessage":
                handleSelectionMessage(observable, (SelectionMessage) message);
                break;
            default: break;
        }
    }
}

