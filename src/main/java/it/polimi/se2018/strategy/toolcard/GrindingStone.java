package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

/**
 * This class implements Tool Card #10 "Grinding Stone" which lets the player flip a die to
 * its opposite side.
 */
public class GrindingStone implements ToolCardEffectStrategy, Serializable {

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
