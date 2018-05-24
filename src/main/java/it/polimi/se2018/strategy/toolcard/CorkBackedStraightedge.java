package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

public class CorkBackedStraightedge implements ToolCardEffectStrategy {

    private Dice die;
    private WindowPatternCard windowPatternCard;

    public CorkBackedStraightedge(){ }

    public CorkBackedStraightedge CorkBackedStraightedge (CorkBackedStraightedge corkBackedStraightedge, Dice draftedDice) {
        corkBackedStraightedge.die = draftedDice;
        return corkBackedStraightedge;
    }

    @Override
    public int executeEffect() throws NotEmptyWindowCellException {
        WindowCell wc = windowPatternCard.getCell(); //una qualsiasi cella scelta dal giocatore che rispetti le condizioni degli if
                if (wc.getNeighbourCells()==null && wc.getDiagonalCells()==null) {
                    if (wc.isEmpty()) {wc.setAssignedDice(die);}
                    }
        return 1;
    }

}
