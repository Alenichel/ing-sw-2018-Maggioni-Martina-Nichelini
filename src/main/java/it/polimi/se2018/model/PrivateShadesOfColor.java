package it.polimi.se2018.model;


import it.polimi.se2018.enumeration.DiceColor;

public class PrivateShadesOfColor {
    private final DiceColor dc;
    String color;

    public PrivateShadesOfColor (DiceColor type){
        this.dc = type;
        this.color = this.dc.toString();
    }

    private boolean isColor(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getColor()== color) return true;
            else return false;
        }
    }

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
