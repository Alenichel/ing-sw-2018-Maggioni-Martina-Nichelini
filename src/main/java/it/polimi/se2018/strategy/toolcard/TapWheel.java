package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

public class TapWheel implements ToolCardEffectStrategy, Serializable {
    public TapWheel(){

    }

    public int executeEffect(){
        return 0;
    }

}
