package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #12 "Tap Wheel" which lets the player move up to two dice of the
 * same color that match the color of a die on the Round Track obeying all restrictions.
 */

public class TapWheel implements ToolCardEffectStrategy, Serializable {

    public TapWheel(){
    }

    public TapWheel TapWheel(TapWheel tapWheel) {
        return tapWheel;
    }

    @Override
    public int executeEffect(){

        return 0;
    }
}