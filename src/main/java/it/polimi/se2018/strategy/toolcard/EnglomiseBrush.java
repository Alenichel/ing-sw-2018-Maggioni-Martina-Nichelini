package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

public class EnglomiseBrush implements ToolCardEffectStrategy {

    private WindowPatternCard windowPatternCard;
    private WindowCell start;
    private WindowCell end;

    public EnglomiseBrush(){ }

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
            this.windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, true);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }

        return 0;
    }
}
