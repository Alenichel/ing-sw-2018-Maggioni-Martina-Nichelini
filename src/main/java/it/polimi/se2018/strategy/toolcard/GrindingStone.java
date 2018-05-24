package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

public class GrindingStone implements ToolCardEffectStrategy{

    private Dice draftedDice;

    public GrindingStone(){ }

    public GrindingStone GrindingStone(GrindingStone grindingStone, Dice draftedDice){
        grindingStone.draftedDice = draftedDice;
        return grindingStone;
    }


    @Override
    public int executeEffect() {
        draftedDice.setNumber(7 - draftedDice.getNumber());
        return 1;
    }
}
