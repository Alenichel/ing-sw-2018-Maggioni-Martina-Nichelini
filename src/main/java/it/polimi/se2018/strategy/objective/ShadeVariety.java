package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

import static java.lang.Integer.min;

/**
 * This class implements Shade Variety objective card which gives you points every time you have
 * a complete set of 6 dice of different number anywhere on your window pattern card
 */
public class ShadeVariety implements ScorePointStrategy, Serializable {

    /**
     * This method verifies if the value of the die on the window cell a is 1
     * @param a
     * @return true if the condition in respected
     */
    private boolean isOne(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getNumber() == 1);
        }
    }

    /**
     * This method verifies if the value of the die on the window cell a is 2
     * @param a
     * @return true if the condition in respected
     */
    private boolean isTwo(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getNumber() == 2);
        }
    }

    /**
     * This method verifies if the value of the die on the window cell a is 3
     * @param a
     * @return true if the condition in respected
     */
    private boolean isThree(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getNumber() == 3);
        }
    }

    /**
     * This method verifies if the value of the die on the window cell a is 4
     * @param a
     * @return true if the condition in respected
     */
    private boolean isFour(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getNumber() == 4);
        }
    }

    /**
     * This method verifies if the value of the die on the window cell a is 5
     * @param a
     * @return true if the condition in respected
     */
    private boolean isFive(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return (a.getAssignedDice().getNumber() == 5);
        }
    }

    /**
     * This method verifies if the value of the die on the window cell a is 6
     * @param a
     * @return true if the condition in respected
     */
    private boolean isSix(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            return(a.getAssignedDice().getNumber()== 6);
        }
    }

    /**
     * This method finds out which of the six integers given is the minimum
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @return minimum
     */
    private int sixMin(int a, int b, int c, int d, int e, int f) {
        int min = min(a,b);
        if (c<min) {min = c;}
        if (d<min) {min = d;}
        if (e<min) {min = e;}
        if (f<min) {min = f;}

        return min;
    }

    /**
     * This methods calculates the score depending on the number of sets containing 6 dice with all different
     * values.
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card (5 per set)
     */
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
            for (int j = 0; j < 5; j++) {
                if (isOne(grid[i][j])) {
                    one += 1;
                }
                if (isTwo(grid[i][j])) {
                    two +=1;
                }
                if (isThree(grid[i][j])) {
                    three +=1;
                }
                if (isFour(grid[i][j])) {
                    four +=1;
                }
                if (isFive(grid[i][j])) {
                    five +=1;
                }
                if (isSix(grid[i][j])) {
                    six +=1;
                }
            }

        scoreCounter = 5*sixMin(one, two, three, four, five, six);
        return scoreCounter;
    }
}
