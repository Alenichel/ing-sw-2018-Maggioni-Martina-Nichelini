package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.DiceLocation;

public class Lathekin implements ToolCardEffectStrategy {

    private WindowPatternCard windowPatternCard;
    private WindowCell start1;
    private WindowCell start2;
    private WindowCell end1;
    private WindowCell end2;


    public Lathekin(){ }

    public Lathekin Lathekin(Lathekin lathekin, WindowPatternCard windowPatternCard, WindowCell s1, WindowCell s2, WindowCell e1, WindowCell e2) {
        lathekin.windowPatternCard = windowPatternCard;
        lathekin.start1 = s1;
        lathekin.start2 = s2;
        lathekin.end1 = e1;
        lathekin.end2 = e2;
        return  lathekin;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException{

        if(start1.isEmpty() || start2.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end1.isEmpty() || !end2.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = this.start1.getAssignedDice();
        Dice d2 = this.start2.getAssignedDice();
        try {
            this.windowPatternCard.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, true);
            this.windowPatternCard.insertDice(d2, end2.getRow(), end2.getColumn(), true, true, true);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }

        return 0;
    }

}
