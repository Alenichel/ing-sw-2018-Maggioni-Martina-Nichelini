package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;

import static java.lang.Integer.min;

/**
 * This class implements Color Variety objective card which gives you points every time you have
 * a complete set of 5 dice of different colors anywhere on your window pattern card
 */
public class ColorVariety implements ScorePointStrategy, Serializable{

    /**
     * This method verifies if the die on the window cell a is red
     * @param a window cell mentioned above
     * @return true if the condition in respected
     */
    private boolean isRed(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getColor().equals("red"));
        }
    }

    /**
     * This method verifies if the die on the window cell a is yellow
     * @param a window cell mentioned above
     * @return true if the condition in respected
     */
    private boolean isYellow(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getColor().equals("yellow"));
        }
    }

    /**
     * This method verifies if the die on the window cell a is blue
     * @param a window cell mentioned above
     * @return true if the condition in respected
     */
    private boolean isBlue(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getColor().equals("blue"));
        }
    }

    /**
     * This method verifies if the die on the window cell a is green
     * @param a window cell mentioned above
     * @return true if the condition in respected
     */
    private boolean isGreen(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getColor().equals("green"));
        }
    }

    /**
     * This method verifies if the die on the window cell a is purple
     * @param a window cell mentioned above
     * @return true if the condition in respected
     */
    private boolean isPurple(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDie()==null) return false;
        else {
            return (a.getAssignedDie().getColor().equals("purple"));
        }
    }

    /**
     * This method finds out which of the five integers given is the minimum
     * @param a int
     * @param b int
     * @param c int
     * @param d int
     * @param e int
     * @return minimum
     */
    private int fiveMin(int a, int b, int c, int d, int e) {
        int min = min(a,b);
        if (c<min) {min = c;}
        if (d<min) {min = d;}
        if (e<min) {min = e;}

        return min;
    }

    /**
     * This methods calculates the score depending on the number of sets containing 5 dice with all different
     * colors.
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card (4 per set)
     */
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
                if (isRed(grid[i][j])) red += 1;
                else if (isYellow(grid[i][j])) yellow +=1;
                else if (isBlue(grid[i][j]))  blue +=1;
                else if (isGreen(grid[i][j])) green +=1;
                else if (isPurple(grid[i][j])) purple +=1;
            }

        scoreCounter = 4*fiveMin(red, yellow, blue, green, purple);
        return scoreCounter;
    }
}