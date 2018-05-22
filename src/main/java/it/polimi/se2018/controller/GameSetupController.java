package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.strategy.toolcard.GlazingHammer;
import it.polimi.se2018.strategy.toolcard.GrindingStone;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSetupController {

    private ArrayList<WindowPatternCard> initializePatternCard(){
        ArrayList<String> windowsPatternCardsName = new ArrayList<>();
        ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();

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
            initializedPatternCards.add(new WindowPatternCard(str));
        }
        return initializedPatternCards;
    }

    private ArrayList<Dice> initializeDiceBag(){
        ArrayList<Dice> diceBag = new ArrayList<>();
        for(Dice dice : diceBag){
            dice.setLocation("bag");
            dice.rollDice();
        }
        return diceBag;
    }

    private  ArrayList<ToolCard> initializeToolCard(){
        ArrayList<ToolCard> toolCards = new ArrayList<>();
        /*toolCards.add(new ToolCard(new GlazingHammer()));
        toolCards.add(new ToolCard(new GrindingStone()));
        toolCards.add(new ToolCard(new GlazingHammer()));*/
        return toolCards;
    }

    public void initializePlayers(Game game){
        for(Player player : game.getPlayers()){
            player.setNumberOfFavorTokens(player.getActivePatternCard().getNumberOfFavorTokens());

            //da finire il setup
        }
    }



    public void initialize(Game game){
        game.setPatternCards(initializePatternCard());
        game.setDiceBag(initializeDiceBag());

    }



}
