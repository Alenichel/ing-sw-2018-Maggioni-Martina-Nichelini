package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

import static java.lang.Integer.min;

public class ColorVariety implements ScorePointStrategy {

    private boolean isRed(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== "red") return true;
            else return false;
        }
    }

    private boolean isYellow(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== "yellow") return true;
            else return false;
        }
    }

    private boolean isBlue(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== "blue") return true;
            else return false;
        }
    }

    private boolean isGreen(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== "green") return true;
            else return false;
        }
    }

    private boolean isPurple(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== "purple") return true;
            else return false;
        }
    }

    private int fiveMin(int a, int b, int c, int d, int e) {
        int min = min(a,b);
        if (c<min) {min = c;}
        if (d<min) {min = d;}
        if (e<min) {min = e;}

        return min;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int red = 0;
        int yellow = 0;
        int blue = 0;
        int green = 0;
        int purple = 0;
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isRed(grid[i][j])) {
                    red += 1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isYellow(grid[i][j])) {
                    yellow +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isBlue(grid[i][j])) {
                    blue +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isGreen(grid[i][j])) {
                    green +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isPurple(grid[i][j])) {
                    purple +=1;
                }

        scoreCounter = 4*fiveMin(red, yellow, blue, green, purple);
        return scoreCounter;
    }
}
