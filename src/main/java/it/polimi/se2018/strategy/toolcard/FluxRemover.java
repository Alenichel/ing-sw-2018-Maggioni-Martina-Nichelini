package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

public class FluxRemover implements ToolCardEffectStrategy, Serializable {
    public FluxRemover(){

    }

    public int executeEffect(){
        return 0;
    }
}
