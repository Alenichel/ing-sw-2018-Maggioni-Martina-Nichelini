package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

/**
 * This class implements Tool Card #8 "Running Pliers" which lets the player choose a second die
 * right after his first turn.
 * The player will skip his second turn.
 */
public class RunningPliers implements ToolCardEffectStrategy, Serializable {

    private Dice draftedDice;

    public RunningPliers(){
    }

    public RunningPliers RunningPliers (RunningPliers runningPliers, Dice draftedDice) {
        runningPliers.draftedDice = draftedDice;
        return runningPliers;
    }

    @Override
    public int executeEffect(){
        return 0;
    }

}
