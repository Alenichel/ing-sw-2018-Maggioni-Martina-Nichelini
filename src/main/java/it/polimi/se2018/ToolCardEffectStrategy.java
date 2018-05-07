package it.polimi.se2018;

import it.polimi.se2018.Exception.ToolCardException;

public interface ToolCardEffectStrategy {
    public int executeEffect() throws ToolCardException;
}
