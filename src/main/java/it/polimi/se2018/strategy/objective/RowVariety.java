package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

/**
 * This class implements objective card #6 ("Row Color Variety") and 5 ("Row Shade Variety")
 */
public class RowVariety implements ScorePointStrategy {

    private VarietyType type;

    public RowVariety(VarietyType type){
        this.type = type;
    }

    private boolean compareCellsColor(WindowCell a, WindowCell b) {
        if (a == null || b == null) return true;
        if (a.equals(b)) return false; //ignore if the method is comparison the same cell
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return true;
        else {
            if ( type == VarietyType.COLOR)
                return (a.getAssignedDice().getColor() == b.getAssignedDice().getColor());
            else
                return (a.getAssignedDice().getNumber() == b.getAssignedDice().getNumber());
        }
    }

    /**
     * This methods calculates a score depending on the number of lines containing 5 dice with all different
     * number.
     * @param windowPatternCard
     * @return Score
     */
    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        boolean allDifferent;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int i = 0; i < 4; i++) {
            allDifferent = true;
            for (int j = 0; j < 5; j++) {
                if ((compareCellsColor(grid[i][j], grid[i][0])) ||
                        (compareCellsColor(grid[i][j], grid[i][1])) ||
                        (compareCellsColor(grid[i][j], grid[i][2])) ||
                        (compareCellsColor(grid[i][j], grid[i][3])) ||
                        (compareCellsColor(grid[i][j], grid[i][4])))
                {
                    allDifferent = false;
                }
            }
            if (allDifferent) scoreCounter += 6;
        }
        return scoreCounter;
    }

}