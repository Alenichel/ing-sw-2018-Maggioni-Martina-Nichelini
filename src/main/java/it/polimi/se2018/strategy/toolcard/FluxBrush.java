package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.DiceLocation;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #6 "Flux Brush" which lets the player re-roll a drafted die.
 * If it can not be placed, return it to the drafted pool.
 */
public class FluxBrush implements ToolCardEffectStrategy, Serializable {

    private Dice die;
    private boolean notPlaced;

    public FluxBrush(){
    }

    public FluxBrush FluxBrush(FluxBrush fluxBrush, Dice die, boolean notPlaced){
        fluxBrush.notPlaced = notPlaced;
        fluxBrush.die = die;
        return fluxBrush;
    }

    /**
     * Set notPlaced to true if the die can not be placed
     */

    @Override
    public int executeEffect(){
        this.die.rollDice();
        if (notPlaced) {die.setLocation(DiceLocation.TABLE);}
        return 1;

    }
}
