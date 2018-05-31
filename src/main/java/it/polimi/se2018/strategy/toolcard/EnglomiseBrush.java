package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.DiceLocation;
import it.polimi.se2018.utils.ObjectiveCardsName;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;
/**
 * This class implements Tool Card #2 "Englomise Brush" which lets the player move a die
 * ignoring color constraints
 */

public class EnglomiseBrush implements ToolCardEffectStrategy, Serializable {

    private WindowPatternCard windowPatternCard;
    private WindowCell start;
    private WindowCell end;

    public EnglomiseBrush(){
    }

    public EnglomiseBrush EnglomiseBrush(EnglomiseBrush englomiseBrush, WindowPatternCard windowPatternCard, WindowCell s, WindowCell e) {
        englomiseBrush.windowPatternCard = windowPatternCard;
        englomiseBrush.start = s;
        englomiseBrush.end = e;
        return englomiseBrush;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException{
        if(start.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = this.start.getAssignedDice();
        try {
            start.removeDice();
            this.windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            start.setAssignedDice(d1);
            throw e;
        }

        //start.setAssignedDice(null);

        return 0;
    }
}
