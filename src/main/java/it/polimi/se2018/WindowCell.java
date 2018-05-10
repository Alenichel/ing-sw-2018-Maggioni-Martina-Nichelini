package it.polimi.se2018;

import java.awt.*;
import java.util.ArrayList;

public class WindowCell {
    private int row;
    private int column;
    private String colorConstraint;
    private int numberConstraint;
    private Dice assignedDice;

    private ArrayList<WindowCell> neighbourCells = new ArrayList<>();

    public WindowCell(){
    }

    private void setNeighbours(WindowCell[][] grid, int x, int y){
        if (x - 1 >= 0 ) neighbourCells.add(grid[x-1][y]);
        if (x + 1 < 4 ) neighbourCells.add(grid[x+1][y]);
        if (y - 1 >= 0 ) neighbourCells.add(grid[x][y-1]);
        if (y + 1 < 5 ) neighbourCells.add(grid[x][y+1]);
    }

    public WindowCell(WindowCell[][] grid, int row, int column, int numberConstraint) {
        setNeighbours(grid, row, column);
        this.row = row;
        this.column = column;
        this.numberConstraint = numberConstraint;
        this.colorConstraint = null;
    }

    public WindowCell(WindowCell[][] grid, int row, int column, String colorConstraint){
        setNeighbours(grid, row, column);
        this.row = row;
        this.column = column;
        this.numberConstraint = 0;
        this.colorConstraint = colorConstraint;
    }

    public String getColorConstraint() {
        return colorConstraint;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getNumberConstraint() {
        return numberConstraint;
    }

    public Dice getAssignedDice() {
        return assignedDice;
    }

    public ArrayList<WindowCell> getNeighbourCells() {
        return neighbourCells;
    }

    public void setAssignedDice(Dice assignedDice) {
        this.assignedDice = assignedDice;
    }

    public void setColorConstraint(String colorConstraint){
         this.colorConstraint = colorConstraint;
    }

    public void setNumberConstraint (int numberConstraint){
        this.numberConstraint = numberConstraint;
    }

    public boolean isEmpty(){
        return null == assignedDice;
    }
}

