package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #9 "Cork Backed Straightedge" which lets the player place a
 * drafted die in a spot that is not adjacent to another die
 */
public class CorkBackedStraightedge implements ToolCardEffectStrategy, Serializable {

    private WindowCell wc;
    private Dice draftedDie;
    private WindowPatternCard windowPatternCard;

    public CorkBackedStraightedge(){
   }

    public CorkBackedStraightedge CorkBackedStraightedge (CorkBackedStraightedge corkBackedStraightedge, WindowCell wc, Dice die, WindowPatternCard windowPatternCard) {
        corkBackedStraightedge.windowPatternCard = windowPatternCard;
        corkBackedStraightedge.wc = wc;
        corkBackedStraightedge.draftedDie = die;
        return corkBackedStraightedge;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException {

        if (!wc.isEmpty())
            throw new ToolCardException("not empty window cell");

        if (wc.getNeighbourCells()!=null && wc.getDiagonalCells()!=null)
            throw new ToolCardException("adjacent window cells not empty");

        try {
            this.windowPatternCard.insertDice(draftedDie, wc.getRow(), wc.getColumn(), true, true, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }
        return 1;
    }
}