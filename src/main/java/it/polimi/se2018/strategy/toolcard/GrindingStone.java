package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.ToolCardEffectStrategy;

public class GrindingStone implements ToolCardEffectStrategy{
    Dice draftedDice;

    public GrindingStone(Dice draftedDice){
        this.draftedDice = draftedDice;
    }

    public GrindingStone(GrindingStone grindingStone, Dice draftedDice){
        this.draftedDice = draftedDice;
    }


    @Override
    public int executeEffect() {
        draftedDice.setNumber(7 - draftedDice.getNumber());
        return 1;
    }
}
