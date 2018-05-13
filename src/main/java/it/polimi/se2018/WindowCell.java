package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;

import java.util.ArrayList;
import java.util.Objects;

public class WindowCell {
    private int row;
    private int column;
    private String colorConstraint;
    private int numberConstraint;
    private Dice assignedDice;

    private ArrayList<WindowCell> neighbourCells = new ArrayList<>();
    private boolean neighbourhoodAssigned = false;


    public WindowCell(int row, int column){
        this.row = row;
        this.column = column;
        this.colorConstraint = null;
        this.numberConstraint = 0;
    }

    public WindowCell(int row, int column, int numberConstraint) {
        this.row = row;
        this.column = column;
        this.numberConstraint = numberConstraint;
        this.colorConstraint = null;
    }

    public WindowCell(int row, int column, String colorConstraint){
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


    public void setAssignedDice(Dice assignedDice) throws NotEmptyWindowCellException{
        if (this.assignedDice == null) this.assignedDice = assignedDice;
        else throw new NotEmptyWindowCellException("Not empty");
    }


    public boolean isEmpty(){
        return null == assignedDice;
    }

    public void setNeighbours(WindowCell[][] grid){
        //this method has to be executed only once
        if (!neighbourhoodAssigned) {
            this.neighbourhoodAssigned = true;
            int x = this.row;
            int y = this.column;

            if (grid == null) return;
            if (x - 1 >= 0) neighbourCells.add(grid[x - 1][y]);
            if (x + 1 < 4) neighbourCells.add(grid[x + 1][y]);
            if (y - 1 >= 0) neighbourCells.add(grid[x][y - 1]);
            if (y + 1 < 5) neighbourCells.add(grid[x][y + 1]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowCell that = (WindowCell) o;
        return row == that.row &&
                column == that.column &&
                numberConstraint == that.numberConstraint &&
                neighbourhoodAssigned == that.neighbourhoodAssigned &&
                Objects.equals(colorConstraint, that.colorConstraint) &&
                Objects.equals(assignedDice, that.assignedDice) &&
                Objects.equals(neighbourCells, that.neighbourCells);
    }

    @Override
    public int hashCode() {

        return Objects.hash(row, column, colorConstraint, numberConstraint, assignedDice, neighbourCells, neighbourhoodAssigned);
    }
}

