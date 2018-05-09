package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class ColumnColorVariety implements ScorePointStrategy {

    private boolean compareCellsColor(WindowCell a, WindowCell b) {
        if (a == null || b == null) return false;
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return false;
        else {
            if (a.getAssignedDice().getColor() == b.getAssignedDice().getColor()) return true;
            else return false;
        }
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 4; i++)
                if ((i != j && compareCellsColor(grid[i][j], grid[0][j])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[1][j])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[2][j])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[3][j])))
                {scoreCounter += 5;}
        return scoreCounter;
    }
}