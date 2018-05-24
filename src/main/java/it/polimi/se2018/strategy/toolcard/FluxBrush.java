package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

public class FluxBrush implements ToolCardEffectStrategy{

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
