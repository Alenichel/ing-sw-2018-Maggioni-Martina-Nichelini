package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class RowShadeVariety implements ScorePointStrategy{

    private boolean compareCellsNumber(WindowCell a, WindowCell b) {
        if (a == null || b == null) return false;
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return false;
        else {
            if (a.getAssignedDice().getNumber() == b.getAssignedDice().getNumber()) return true;
            else return false;
        }
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                if ((i != j && compareCellsNumber(grid[i][j], grid[i][0])) &&
                        (i != j && compareCellsNumber(grid[i][j], grid[i][1])) &&
                        (i != j && compareCellsNumber(grid[i][j], grid[i][2])) &&
                        (i != j && compareCellsNumber(grid[i][j], grid[i][3])) &&
                        (i != j && compareCellsNumber(grid[i][j], grid[i][4])))
                {scoreCounter +=6;}
        return scoreCounter;
    }
}
