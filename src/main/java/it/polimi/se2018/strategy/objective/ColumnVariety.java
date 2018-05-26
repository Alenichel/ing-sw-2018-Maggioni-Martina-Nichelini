package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

public class ColumnVariety implements ScorePointStrategy, Serializable {

    private VarietyType type;

    public ColumnVariety(VarietyType type){
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

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        boolean allDifferent;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int j = 0; j < 5; j++) {
            allDifferent = true;
            for (int i = 0; i < 4; i++) {
                if ((compareCellsColor(grid[i][j], grid[0][j])) ||
                        (compareCellsColor(grid[i][j], grid[1][j])) ||
                        (compareCellsColor(grid[i][j], grid[2][j])) ||
                        (compareCellsColor(grid[i][j], grid[3][j])))
                {
                    allDifferent = false;
                }
            }
            if (allDifferent) scoreCounter += 5;
        }
        return scoreCounter;
    }
}