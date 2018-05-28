package it.polimi.se2018.message;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.utils.DiceLocation;

public class MoveDiceMessage extends Message {

    private DiceLocation startingLocation;
    private DiceLocation endingLocation;

    private int tableCoordinate;
    private int endingX;
    private int endingY;

    public MoveDiceMessage(int tableCoordinate,  String endingCoordinates){
        this.messageType = "MoveDiceMessage";
        this.startingLocation = DiceLocation.TABLE;
        this.endingLocation = DiceLocation.WINDOWCELL;
        this.tableCoordinate = tableCoordinate - 1;
        this.endingX = Integer.parseInt(endingCoordinates.substring(0,1)) - 1;
        this.endingY = Integer.parseInt(endingCoordinates.substring(1,2)) - 1;
    }

    public DiceLocation getStartingLocation() {
        return startingLocation;
    }

    public DiceLocation getEndingLocation() {
        return endingLocation;
    }

    public int getTableCoordinate() {
        return tableCoordinate;
    }

    public int getEndingX() {
        return endingX;
    }

    public int getEndingY() {
        return endingY;
    }
}
