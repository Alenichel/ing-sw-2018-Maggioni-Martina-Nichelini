package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

import static java.lang.Integer.min;

public class ColorVariety implements ScorePointStrategy, Serializable {

    private boolean isRed(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getColor()== "red");
        }
    }

    private boolean isYellow(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getColor()== "yellow");
        }
    }

    private boolean isBlue(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getColor()== "blue");
        }
    }

    private boolean isGreen(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getColor()== "green");
        }
    }

    private boolean isPurple(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getColor()== "purple");
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
            for (int j = 0; j < 5; j++) {
                if (isRed(grid[i][j])) {
                    red += 1;
                }
                if (isYellow(grid[i][j])) {
                    yellow +=1;
                }
                if (isBlue(grid[i][j])) {
                    blue +=1;
                }
                if (isGreen(grid[i][j])) {
                    green +=1;
                }
                if (isPurple(grid[i][j])) {
                    purple +=1;
                }
            }

        scoreCounter = 4*fiveMin(red, yellow, blue, green, purple);
        return scoreCounter;
    }
}
