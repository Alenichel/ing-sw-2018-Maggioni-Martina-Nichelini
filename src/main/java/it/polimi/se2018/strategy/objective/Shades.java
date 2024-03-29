package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

import static java.lang.Integer.min;

/**
 * These cards allow you to score points every time you have a set of light (1, 2),
 * medium (3, 4) or dark (5, 6) shades on your window pattern card
 */

public class Shades implements ScorePointStrategy, Serializable {

    int firstNumber;
    int secondNumber;

    /**
     * Class constructor
     * @param type It can be light, medium or dark
     */

    public Shades(String type) {
        if (type.equals("light")) {
            this.firstNumber = 1;
            this.secondNumber = 2;
        }
        else if (type.equals("medium")) {
            this.firstNumber = 3;
            this.secondNumber = 4;
        }
        else if (type.equals("dark")) {
            this.firstNumber = 5;
            this.secondNumber = 6;
        }
        else throw new IllegalArgumentException("Invalid type");
    }

    /**
     * isFirst checks if the dice in the window cell has the SHADE we are searching for on it
     * @param a window cell under consideration
     * @return true if the condition in respected
     */
    private boolean isFirst(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getNumber()== firstNumber);
        }
    }

    /**
     * isSecond checks if the dice in the window cell has the SHADE we are searching for on it
     * @param a window cell under consideration
     * @return true if the condition in respected
     */
    private boolean isSecond(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getNumber()== secondNumber);
        }
    }

    /**
     * For every set of light, medium or dark shades found, this method adds 2 points
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card
     */

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int first = 0;
        int second = 0;
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isFirst(grid[i][j])) {
                    first += 1;
                }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isSecond(grid[i][j])) {
                    second +=1;
                }
        scoreCounter = 2*min(first, second);
        return scoreCounter;
    }
}
