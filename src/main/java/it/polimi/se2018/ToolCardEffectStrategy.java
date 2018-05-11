package it.polimi.se2018;

import it.polimi.se2018.exception.ToolCardException;

public interface ToolCardEffectStrategy {
    public int executeEffect() throws ToolCardException;
}
