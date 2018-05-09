package it.polimi.se2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

public class GameController implements Observer{
    private Game gameReference;

    public GameController(Game gameReference){
        ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();
        this.gameReference = gameReference;
        initializedPatternCards = initializedPatternCard();
    }

    public ArrayList<WindowPatternCard> initializedPatternCard(){
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

        ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();

        for (String str : windowsPatternCardsName){
            initializedPatternCards.add(new WindowPatternCard(str));
        }
    return initializedPatternCards;
    }



    public void gameSetupHandler(){
        /*
      -- istanzia tutte le carte obiettivi (pubbliche e private)
      -- istanzia la room con i giocatori
      -- istanzia tutti i dati nella bag
      -- istanzia 3 carte tool
      -- istanzia il turn robo

      */
    }

    public void gameHandler(){

    }

    public void endingHandler(){

    }

   @Override
    public void update(Observable observable, Object msg){

    }
}
