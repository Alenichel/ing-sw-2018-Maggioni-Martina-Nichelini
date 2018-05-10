package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class ColorDiagonals implements ScorePointStrategy {

    private boolean compareCellsColor (WindowCell a, WindowCell b) {
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

        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                if (compareCellsColor(grid[i][j], grid[i-1][j-1]) ||
                    compareCellsColor(grid[i][j], grid [i+1][j+1]) ||
                    compareCellsColor(grid[i][j], grid [i+1][j-1]) ||
                    compareCellsColor(grid[i][j], grid [i-1][j+1])) {scoreCounter += 1;}

        return scoreCounter;
    }
}
