package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements Tool Card #7 "Glazing Hammer" which lets the player re-roll all the dice
 * in the draft pool.
 * This may only be used on the second turn before drafting
 */
public class GlazingHammer implements ToolCardEffectStrategy, Serializable {

    private ArrayList<Dice> draftedDice;

    public GlazingHammer(){ }

    public GlazingHammer GlazingHammer(GlazingHammer glazingHammer, ArrayList<Dice> draftedDice){
        glazingHammer.draftedDice = draftedDice;
        return glazingHammer;
    }

    @Override
    public int executeEffect() { //può essere usata solo durante il secondo turno
        for(Dice dice : this.draftedDice){
            dice.rollDice();
        }
        return 1;
    }
}
