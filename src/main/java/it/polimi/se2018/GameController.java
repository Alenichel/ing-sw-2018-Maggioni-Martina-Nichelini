package it.polimi.se2018;

import it.polimi.se2018.message.*;

import java.io.InvalidClassException;
import java.util.*;

public class GameController implements Observer{
    private ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();

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

    public GameController(){
        initializedPatternCards = this.initializePatternCard();
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

        for (String str : windowsPatternCardsName){
            initializedPatternCards.add(new WindowPatternCard(str));
        }
        return initializedPatternCards;
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
                if (((RequestMessage)msg).getRequest().equalsIgnoreCase("PatternCardPool")){
                    ((View)observable).requestCallback(new GiveMessage("PatternCardPool", this.getRandomPatternCards()));
                }
                break;


            default: break;
        }
    }
}
