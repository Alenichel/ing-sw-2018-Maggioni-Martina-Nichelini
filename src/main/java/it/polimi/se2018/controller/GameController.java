package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.TimerInterface;
import it.polimi.se2018.view.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.*;

public class GameController implements Observer, Serializable{
    private ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();
    private Game gameAssociated;

    private List<WindowPatternCard> getRandomPatternCards (){
       int selectedIndex = 0;
       ArrayList<WindowPatternCard> toReturn = new ArrayList<>();
       Random rand = new Random();
       for (int i = 0; i < 4; i++){
           selectedIndex = rand.nextInt(this.initializedPatternCards.size());
           toReturn.add(this.initializedPatternCards.get(selectedIndex));
           this.initializedPatternCards.remove(selectedIndex);
       }
       return toReturn;

   }

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

    public GameController(Game game){
        initializedPatternCards = this.initializePatternCard();
        this.gameAssociated = game;
    }

    private void onPatternCardSelection(Game game, SelectionMessage message) throws InvalidClassException {
        if (message.getChosenItem() instanceof WindowPatternCard ){
            Player targetPlayer = game.getPlayers().get(message.getPlayerNumber());
            targetPlayer.setActivePatternCard((WindowPatternCard)(message.getChosenItem()));
            game.addWindowPatternCard((WindowPatternCard) message.getChosenItem());
            //this.gameReference.notifyAll();
        } else {
            throw new InvalidClassException("Received: " + message.getChosenItem() + "but requested WindowPatternCard");
        }
    }


   @Override
    public void update(Observable observable, Object msg){

        switch(((Message)msg).getMessageType()){

            case "RequestMessage":
                RequestMessage rMsg = ((RequestMessage)msg);
                if (rMsg.getRequest().equalsIgnoreCase("PatternCardPool")){
                    ((VirtualView)observable).controllerCallback(new GiveMessage("PatternCardPool", this.getRandomPatternCards()));
                }
                else if (rMsg.getRequest().equals("PlayerInGame")){
                    ((VirtualView) observable).controllerCallback(new GiveMessage("PlayerInGame", gameAssociated.getPlayers()));
                }
                break;


            default: break;
        }
    }


}
