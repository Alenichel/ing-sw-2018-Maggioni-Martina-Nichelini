package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class RowVariety implements ScorePointStrategy {

    private String type;

    public RowVariety(String type){
        if (type.equals("Color") || type.equals("Shade")){
            this.type = type;
        }
    }

    private boolean compareCellsColor(WindowCell a, WindowCell b) {
        if (a == null || b == null) return true;
        if (a.equals(b)) return false; //ignore if the method is comparison the same cell
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return true;
        else {
            if ( type == "Color")
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