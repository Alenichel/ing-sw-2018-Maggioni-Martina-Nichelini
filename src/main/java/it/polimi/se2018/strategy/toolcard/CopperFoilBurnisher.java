package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

public class CopperFoilBurnisher implements ToolCardEffectStrategy {

    private WindowPatternCard windowPatternCard;

    public CopperFoilBurnisher() { }

    public CopperFoilBurnisher CopperFoilBurnisher (CopperFoilBurnisher copperFoilBurnisher, WindowPatternCard windowPatternCard) {
        copperFoilBurnisher.windowPatternCard = windowPatternCard;
        return  copperFoilBurnisher;
    }

    @Override
    public int executeEffect(){
        //dado che il giocatore sceglie di spostare
        Dice dice = windowPatternCard.getCell().getAssignedDice();
        //se la cella di arrivo ha una restrizione di valore, la ignoro
        if (windowPatternCard.getCell().getNumberConstraint() != null) {windowPatternCard.getCell().setNumberConstraint() == null}
        //controllo che cella di partenza e di arrivo non coincidano e sposto il dado
        if(windowPatternCard.getCell() != DiceLocation.WINDOWCELL){
            dice.setLocation(DiceLocation.WINDOWCELL);
        }
        return 0;
    }

}
