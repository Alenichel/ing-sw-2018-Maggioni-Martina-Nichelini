package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.DiceLocation;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSetupController implements Serializable {

    private Game associatedGame;

    GameSetupController(Game game){
        this.associatedGame = game;
    }


    private void initializePatternCard(){
        ArrayList<String> windowsPatternCardsName = new ArrayList<>();

        windowsPatternCardsName.add("virtus");
        windowsPatternCardsName.add("viaLux");
        windowsPatternCardsName.add("bellesguard");
        windowsPatternCardsName.add("keleidoscopicDream");
        windowsPatternCardsName.add("auroraeMagnificus");
        windowsPatternCardsName.add("sunCatcher");
        windowsPatternCardsName.add("shadowThief");
        windowsPatternCardsName.add("auroraSagradis");
        windowsPatternCardsName.add("firmitas");
        windowsPatternCardsName.add("batllo");
        windowsPatternCardsName.add("industria");
        windowsPatternCardsName.add("symhonyOfLight");
        windowsPatternCardsName.add("chromaticSplendor");
        windowsPatternCardsName.add("comitas");
        windowsPatternCardsName.add("firelight");
        windowsPatternCardsName.add("fractalDrops");
        windowsPatternCardsName.add("fulgorDelCielo");
        windowsPatternCardsName.add("gravitas");
        windowsPatternCardsName.add("luxAstram");
        windowsPatternCardsName.add("luxMundi");
        windowsPatternCardsName.add("luzCelestial");
        windowsPatternCardsName.add("ripplesOfLight");
        windowsPatternCardsName.add("sunsGlory");
        windowsPatternCardsName.add("waterOfLife");

        for (String str : windowsPatternCardsName){
            this.associatedGame.getPatternCards().add(new WindowPatternCard(str));
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
