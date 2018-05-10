package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

import static java.lang.Integer.min;

public class ShadeVariety implements ScorePointStrategy {

    private boolean isOne(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 1) return true;
            else return false;
        }
    }

    private boolean isTwo(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 2) return true;
            else return false;
        }
    }

    private boolean isThree(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 3) return true;
            else return false;
        }
    }

    private boolean isFour(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 4) return true;
            else return false;
        }
    }

    private boolean isFive(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 5) return true;
            else return false;
        }
    }

    private boolean isSix(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== 6) return true;
            else return false;
        }
    }

    private int sixMin(int a, int b, int c, int d, int e, int f) {
        int min = min(a,b);
        if (c<min) {min = c;}
        if (d<min) {min = d;}
        if (e<min) {min = e;}
        if (f<min) {min = f;}

        return min;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isOne(grid[i][j])) {
                    one += 1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isTwo(grid[i][j])) {
                    two +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isThree(grid[i][j])) {
                    three +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isFour(grid[i][j])) {
                    four +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isFive(grid[i][j])) {
                    five +=1;
                }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isSix(grid[i][j])) {
                    six +=1;
                }

        scoreCounter = 5*sixMin(one, two, three, four, five, six);
        return scoreCounter;
    }
}
