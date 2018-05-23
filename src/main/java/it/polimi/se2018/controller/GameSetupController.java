package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.toolcard.*;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.DiceLocation;
import it.polimi.se2018.utils.ToolCardsName;
import it.polimi.se2018.utils.WindowPatternCardsName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GameSetupController implements Serializable {

    private Game associatedGame;

    public GameSetupController(Game game){
        this.associatedGame = game;
    }

    /**
     * This method initialize all windows pattern cards, before asking player wich card he wants to use during this game
     */
    private void initializePatternCard(){
        for(WindowPatternCardsName w : WindowPatternCardsName.values()){
            this.associatedGame.getPatternCards().add(new WindowPatternCard(w.toString()));
        }
    }

    private void initializeToolCards(){
        int n;
        ArrayList<String> toolName = new ArrayList<>();
        ArrayList<ToolCard> selectedToolCards = new ArrayList<ToolCard>();
        for(ToolCardsName t : ToolCardsName.values()){
            toolName.add(t.toString());
        }

        Random rand = new Random();
        n = rand.nextInt(4)+1;

        for(int i = 0; i<3; i++){
            n = rand.nextInt(toolName.size());
            selectedToolCards.add(new ToolCard(nameToObject(toolName.get(n))));
            toolName.remove(n);
        }
    }

    private ToolCardEffectStrategy nameToObject(String name){
        switch (name){
            case "GrozingPliers" : return new GrozingPliers();
            case "EnglomiseBrush" : return new EnglomiseBrush();
            case "CopperFoilBurnisher" : return new CopperFoilBurnisher();
            case "Lathekin" : return new Lathekin();
            case "LensCutter" : return new LensCutter();
            case "FluxBrush" : return new FluxBrush();
            case "GlazingHammer" : return new GlazingHammer();
            case "RunningPliers" : return new RunningPliers();
            case "CorkBackedStraightedge" : return new CorkBackedStraightedge();
            case "GrindingStone" : return new GrindingStone();
            case "FluxRemover" : return new FluxRemover();
            case "TapWheel" : return new TapWheel();
            default : throw new IllegalArgumentException();
        }
    }


    /**
     * This method initialize all dice that will be used during the game.
     * According to Sagrada's rules, 15 dice for each color are initialized and they are put on the diceBag.
     */
    private void diceInitializer(){
        for (DiceColor dc : DiceColor.values()){
            for (int i = 0; i < 15; i++){
                Dice newDice = new Dice(dc.name());
                newDice.setLocation(DiceLocation.BAG);
                this.associatedGame.getDiceBag().add(newDice);
            }
        }
    }

    private  ArrayList<ToolCard> initializeToolCard(){
        ArrayList<ToolCard> toolCards = new ArrayList<>();
        /*toolCards.add(new ToolCard(new GlazingHammer()));
        toolCards.add(new ToolCard(new GrindingStone()));
        toolCards.add(new ToolCard(new GlazingHammer()));*/
        return toolCards;
    }

    private void initializePlayers(Game game){
        for(Player player : game.getPlayers()){
            player.setNumberOfFavorTokens(player.getActivePatternCard().getNumberOfFavorTokens()); //messo dirattemente in player

        }
    }

    public void initialize(){
        this.diceInitializer();
        this.initializePatternCard();
    }

}
