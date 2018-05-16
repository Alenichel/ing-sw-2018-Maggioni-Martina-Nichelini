package it.polimi.se2018.model;


public class PrivateShadesOfColor {
    String color;

    public PrivateShadesOfColor (String type){
        if(type == "red") this.color = "red";
        else if(type == "yellow") this.color = "yellow";
        else if(type == "blue") this.color = "blue";
        else if(type == "green") this.color = "green";
        else if(type == "purple") this.color = "purple";
        else throw new IllegalArgumentException("Invalid type");
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
