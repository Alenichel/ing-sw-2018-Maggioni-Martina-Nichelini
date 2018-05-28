package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #9 "Running Pliers" which lets the player choose a second die
 * right after his first turn.
 * The player will skip his second turn.
 */
public class RunningPliers extends ToolCard implements ToolCardEffectStrategy, Serializable {

    private Dice draftedDice;

    public RunningPliers(){
        this.setName(ToolCardsName.RunningPliers.toString());
        this.setDescription("After your first turn, immediately draft a die. Skip your next turn this round");
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
