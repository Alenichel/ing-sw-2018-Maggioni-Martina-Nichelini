package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #1 "Grozing Pliers" which lets the player  increase or decrease
 * the value of a drafted die by 1
 */
public class GrozingPliers implements ToolCardEffectStrategy, Serializable {

    private Dice draftedDice;
    private boolean increase;

    public GrozingPliers() {
    }

    public GrozingPliers GrozingPliers(GrozingPliers grozingPliers, Dice draftedDice, boolean increase){
        grozingPliers.draftedDice = draftedDice;
        grozingPliers.increase = increase;
        return grozingPliers;
    }


    @Override
    public int executeEffect() {
        if (increase && draftedDice.getNumber() != 6) {
            draftedDice.setNumber(draftedDice.getNumber() + 1);
        } else if (draftedDice.getNumber() != 1) {
            draftedDice.setNumber(draftedDice.getNumber() - 1);
            }
        return 1;
        }
}