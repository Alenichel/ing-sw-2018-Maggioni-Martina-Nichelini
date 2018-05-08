package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class RowColorVariety implements ScorePointStrategy {

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
                if ((i != j && compareCellsColor(grid[i][j], grid[i][0])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[i][1])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[i][2])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[i][3])) &&
                   (i != j && compareCellsColor(grid[i][j], grid[i][4])))
            {scoreCounter +=6;}
        return 0;
    }

}