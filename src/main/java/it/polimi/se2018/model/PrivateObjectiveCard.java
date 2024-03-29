package it.polimi.se2018.model;

import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.enumeration.DiceColor;

/**
 * This class implements PrivateObjectiveCard.
 */
public class PrivateObjectiveCard extends ObjectiveCard {

    private DiceColor color;

    /**
     * Private objective card constructor
     * @param diceColor red, blue, yellow, green or purple
     */
    public PrivateObjectiveCard(DiceColor diceColor){
        this.color = diceColor;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        WindowCell[][] grid = windowPatternCard.getGrid();
        int score = 0;

        for (WindowCell[] line : grid)
            for (WindowCell cell : line)
                try {
                    if (cell.getAssignedDie().getDiceColor().equals(this.color))  score++;
                } catch (NullPointerException e) {continue;}

        return score;
    }

    /**
     * Color getter
     * @return color
     */
    public DiceColor getColor(){
        return this.color;
    }

    @Override
    public String toString() {
        ConsoleUtils c = new ConsoleUtils();
        String str ="";
        str = str.concat("Your private objective: ");
        str = str.concat(c.toUnicodeColor(this.color.toString()) + "\u25FE" + (char) 27 + "[30m");

        return str;
    }
}