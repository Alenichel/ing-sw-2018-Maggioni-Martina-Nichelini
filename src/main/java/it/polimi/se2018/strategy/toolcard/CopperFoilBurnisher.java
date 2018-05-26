package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

import java.io.Serializable;

public class CopperFoilBurnisher implements ToolCardEffectStrategy, Serializable {

    private WindowPatternCard windowPatternCard;
    private WindowCell start;
    private WindowCell end;

    public CopperFoilBurnisher() { }

    public CopperFoilBurnisher CopperFoilBurnisher (CopperFoilBurnisher copperFoilBurnisher, WindowPatternCard windowPatternCard, WindowCell s, WindowCell e) {
        copperFoilBurnisher.windowPatternCard = windowPatternCard;
        copperFoilBurnisher.start = s;
        copperFoilBurnisher.end = e;
        return  copperFoilBurnisher;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException {
        if(start.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = this.start.getAssignedDice();
        try {
            this.windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, false, true);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }

        return 0;
    }
}
