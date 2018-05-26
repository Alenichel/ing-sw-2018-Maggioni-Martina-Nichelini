package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

/**
 * This class implements Tool Card #6 "Flux Brush" which lets the player re-roll a drafted die.
 * If it can not be placed, return it to the drafted pool.
 */
public class FluxBrush implements ToolCardEffectStrategy, Serializable {

    private Dice die;

    public FluxBrush(){ }

    public FluxBrush FluxBrush(FluxBrush fluxBrush, Dice die){
        fluxBrush.die = die;
        return fluxBrush;
    }

    @Override
    public int executeEffect(){
        this.die.rollDice(); //se non è possibile piazzarlo, riporlo nella riserva (table)
        return 1;
    }
}
