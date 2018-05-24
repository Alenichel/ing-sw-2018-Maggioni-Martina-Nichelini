package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

public class EnglomiseBrush implements ToolCardEffectStrategy {

    private WindowPatternCard windowPatternCard;

    public EnglomiseBrush(){ }

    public EnglomiseBrush EnglomiseBrush(EnglomiseBrush englomiseBrush, WindowPatternCard windowPatternCard) {
        englomiseBrush.windowPatternCard = windowPatternCard;
        return englomiseBrush;
    }

    @Override
    public int executeEffect(){
        //dado che il giocatore sceglie di spostare
        Dice dice = windowPatternCard.getCell().getAssignedDice();
        //se la cella di arrivo ha una restrizione di colore, la ignoro
        if (windowPatternCard.getCell().getColorConstraint() != null) {windowPatternCard.getCell().setColorConstraint() == null}
        //controllo che cella di partenza e di arrivo non coincidano e sposto il dado
        if(windowPatternCard.getCell() != DiceLocation.WINDOWCELL){
            dice.setLocation(DiceLocation.WINDOWCELL);
        }
        return 0;
    }
}
