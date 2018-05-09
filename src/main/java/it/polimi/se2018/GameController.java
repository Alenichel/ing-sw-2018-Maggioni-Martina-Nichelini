package it.polimi.se2018;

import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.SelectionMessage;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

public class GameController implements Observer{
    private Game gameReference;

    //manca il metodo che serve le pattern card.

    public GameController(Game gameReference){
        ArrayList<WindowPatternCard> initializedPatternCards = new ArrayList<>();
        this.gameReference = gameReference;
        initializedPatternCards = initializedPatternCard();
    }

    private ArrayList<WindowPatternCard> initializedPatternCard(){
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

    private void onPatternCardSelection(SelectionMessage message) throws InvalidClassException {
        if (message.getChosenItem() instanceof WindowPatternCard ){
            Player targetPlayer = gameReference.getPlayers().get(message.getPlayerNumber());
            targetPlayer.setPatternCardPool(null);
            targetPlayer.setActivePatternCard((WindowPatternCard)(message.getChosenItem()));
            gameReference.addWindowPatternCard((WindowPatternCard) message.getChosenItem());
            this.gameReference.notifyAll();
        } else {
            throw new InvalidClassException("Received: " + message.getChosenItem() + "but requested WindowPatternCard");
        }
    }

   @Override
    public void update(Observable observable, Object msg){

        switch(((Message)msg).getMessageType()){

            case "SelectionMessage":
                try {
                    this.onPatternCardSelection((SelectionMessage) msg);
                } catch (InvalidClassException e){}
                break;

            default:
                throw new RuntimeException();
        }
    }
}
