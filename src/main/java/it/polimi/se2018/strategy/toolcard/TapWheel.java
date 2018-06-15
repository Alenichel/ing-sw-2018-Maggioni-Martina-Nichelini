package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;

import java.io.Serializable;

/**
 * This class implements Tool Card #12 "Tap Wheel" which lets the player move up to two dice of the
 * same color that match the color of a die on the Round Track obeying all restrictions.
 */

public class TapWheel implements ToolCardEffectStrategy, Serializable {

    private WindowPatternCard wpc;
    private WindowCell start1;
    private WindowCell start2;
    private WindowCell end1;
    private WindowCell end2;
    private int number;

    public TapWheel(){
    }

    public TapWheel TapWheel(TapWheel tapWheel, WindowPatternCard wpc, WindowCell start1, WindowCell start2, WindowCell end1, WindowCell end2, int number) {
        tapWheel.start1 = start1;
        tapWheel.start2 = start2;
        tapWheel.end1 = end1;
        tapWheel.end2 = end2;
        tapWheel.number = number;
        return tapWheel;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException {

        if (number != 1 || number != 2)
            throw new ToolCardException("you can move up to 2 dice");

        if(start1.isEmpty() || start2.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end1.isEmpty() || !end2.isEmpty())
            throw new ToolCardException("not empty window cell");

        Dice d1 = this.start1.getAssignedDice();
        Dice d2 = this.start2.getAssignedDice();

        //controllare che d1 sia dello stesso colore di un dado del RoundTrack

        if (number == 1) {
            try {
                this.wpc.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, true);
            }catch (ToolCardException | NotEmptyWindowCellException e) {
                throw e;
            }

            start1.removeDice();
        }

        //controllare che d1 e d2 siano dello stesso colore di un dado del RoundTrack

        if (number == 2) {
            try {
                this.wpc.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, false);
                this.wpc.insertDice(d2, end2.getRow(), end2.getColumn(), true, true, false);
            }catch (ToolCardException | NotEmptyWindowCellException e) {
                throw e;
            }

            start1.removeDice();
            start2.removeDice();
        }

        return 0;
    }
}