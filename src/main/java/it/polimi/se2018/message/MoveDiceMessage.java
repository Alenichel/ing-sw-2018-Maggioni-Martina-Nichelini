package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.DiceLocation;

/**
 * Class for Move dice message
 */
public class MoveDiceMessage extends Message {

    private DiceLocation startingLocation;
    private DiceLocation endingLocation;

    private int tableCoordinate;
    private int endingX;
    private int endingY;

    /**
     * Class constructor
     */
    public MoveDiceMessage(int tableCoordinate,  int endingX, int endingY){
        this.messageType = "MoveDiceMessage";
        this.startingLocation = DiceLocation.TABLE;
        this.endingLocation = DiceLocation.WINDOWCELL;
        this.tableCoordinate = tableCoordinate - 1;
        this.endingX = endingX;
        this.endingY = endingY;
    }

    /**
     * Starting location getter
     * @return starting location
     */
    public DiceLocation getStartingLocation() {
        return startingLocation;
    }

    /**
     * Ending location getter
     * @return ending location
     */
    public DiceLocation getEndingLocation() {
        return endingLocation;
    }

    /**
     * Table coordinate getter
     * @return table coordinate
     */
    public int getTableCoordinate() {
        return tableCoordinate;
    }

    /**
     * Ending x getter
     * @return ending x
     */
    public int getEndingX() {
        return endingX;
    }

    /**
     * Ending y getter
     * @return ending y
     */
    public int getEndingY() {
        return endingY;
    }
}