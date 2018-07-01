package it.polimi.se2018.model;


import it.polimi.se2018.enumeration.DiceColor;

/**
 * This class implements all 5 private objective cards: shades of red, yellow, green, blue, purple.
 */
public class PrivateShadesOfColor {
    private final DiceColor dc;
    String color;

    /**
     * Private shades of color constructor
     * @param type: red, purple, green, blue or yellow
     */
    public PrivateShadesOfColor (DiceColor type){
        this.dc = type;
        this.color = this.dc.toString();
    }

    /**
     * This method verifies if the die on the window cell a has the same color as "type"
     * @param a window cell
     * @return true if the condition is respected
     */
    private boolean isColor(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor().equals(color)) return true;
            else return false;
        }
    }

    /**
     * This methods calculates the score depending on the number of cells containing a die of the
     * private objective card's color.
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card
     */
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isColor(grid[i][j])) {
                    scoreCounter += 1;
                }
        return scoreCounter;
    }
}