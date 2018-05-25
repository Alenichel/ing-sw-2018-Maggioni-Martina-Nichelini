package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

public class Lathekin implements ToolCardEffectStrategy {

    private WindowPatternCard windowPatternCard;

    public Lathekin(){ }

    public Lathekin Lathekin(Lathekin lathekin, WindowPatternCard windowPatternCard) {
        lathekin.windowPatternCard = windowPatternCard;
        return  lathekin;
    }

    @Override
    public int executeEffect(){
        Dice d1 = windowPatternCard.getCell().getAssignedDice(); //primo dado scelto dal giocatore
        Dice d2 = windowPatternCard.getCell().getAssignedDice(); //secondo dado scelto dal giocatore

        //controllo che cella di partenza e di arrivo non coincidano e sposto i dadi
        //la cella di arrivo sarà indicata dal giocatore
        if(windowPatternCard.getCell() != DiceLocation.WINDOWCELL){
            d1.setLocation(DiceLocation.WINDOWCELL);
        }

        //la cella di arrivo sarà indicata dal giocatore
        if(windowPatternCard.getCell() != DiceLocation.WINDOWCELL){
            d2.setLocation(DiceLocation.WINDOWCELL);
        }

        return 0;
    }

}
