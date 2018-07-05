package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.enumeration.VarietyType;
import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

/**
 * This class implements Row Color Variety and Row Shade Variety objective cards
 */
public class RowVariety implements ScorePointStrategy, Serializable {

    private VarietyType type;

    public RowVariety(VarietyType type){
        this.type = type;
    }

    /**
     * This method checks if two cells, a and b, contain a die of the same value or color
     * @param a first cell
     * @param b second cell
     * @return true if the condition in respected
     */
    private boolean compareCells(WindowCell a, WindowCell b) {
        if (a == null || b == null) return true;
        if (a.equals(b)) return false; //ignore if the method is comparison the same cell
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return true;
        else {
            if ( type == VarietyType.COLOR)
                return (a.getAssignedDice().getColor().equals(b.getAssignedDice().getColor()));
            else
                return (a.getAssignedDice().getNumber() == b.getAssignedDice().getNumber());
        }
    }

    /**
     * This methods calculates the score depending on the number of rows containing 5 dice with all different
     * values or colors.
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card
     */
    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        boolean allDifferent;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int i = 0; i < 4; i++) {
            allDifferent = true;
            for (int j = 0; j < 5; j++) {
                if ((compareCells(grid[i][j], grid[i][0])) ||
                        (compareCells(grid[i][j], grid[i][1])) ||
                        (compareCells(grid[i][j], grid[i][2])) ||
                        (compareCells(grid[i][j], grid[i][3])) ||
                        (compareCells(grid[i][j], grid[i][4])))
                {
                    allDifferent = false;
                }
            }
            if (allDifferent) scoreCounter += 6;
        }
        return scoreCounter;
    }

}