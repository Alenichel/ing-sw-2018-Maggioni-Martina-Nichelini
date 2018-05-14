package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.ToolCardEffectStrategy;

public class FluxBrush implements ToolCardEffectStrategy{

    private Dice die;

    public FluxBrush(Dice die){
        this.die = die;
    }

    public FluxBrush FluxBrush(FluxBrush fluxBrush, Dice die){
        fluxBrush.die = die;
        return fluxBrush;
    }

    public int executeEffect(){
        this.die.rollDice();
        return 1;
    }
}
