package it.polimi.se2018.model;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;

/**
 * Tool card effect strategy interface
 */
public interface ToolCardEffectStrategy  {
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException;
}
