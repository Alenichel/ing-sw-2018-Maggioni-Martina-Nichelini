package it.polimi.se2018.model;

import it.polimi.se2018.exception.NotEmptyWindowCellException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * WindowCell class represents the cell, *one square*, of the window grid.
 * Each cell can be assigned with a die.
 */
public class WindowCell implements Serializable{
    private int row;
    private int column;
    private String colorConstraint;
    private int numberConstraint;
    private Dice assignedDice;

    private List<WindowCell> neighbourCells = new ArrayList<>();
    private boolean neighbourhoodAssigned = false;

    private List<WindowCell> diagonalCells = new ArrayList<>();
    private boolean isDiagonal = false;

    /**
     * Constructor for cell without constraints.
     * @param row number of the row
     * @param column number of the column
     */
    public WindowCell(int row, int column){
        this.row = row;
        this.column = column;
        this.colorConstraint = null;
        this.numberConstraint = 0;
    }

    /**
     * Constructor for cell with a number constraint.
     * @param row number of the row
     * @param column number of the column
     * @param numberConstraint number constraint
     */
    public WindowCell(int row, int column, int numberConstraint) {
        this.row = row;
        this.column = column;
        this.numberConstraint = numberConstraint;
        this.colorConstraint = null;
    }

    /**
     * Constructor for cell with a color constraint.
     * @param row number of the row
     * @param column number of the column
     * @param colorConstraint color constraint
     */
    public WindowCell(int row, int column, String colorConstraint){
        this.row = row;
        this.column = column;
        this.numberConstraint = 0;
        this.colorConstraint = colorConstraint;
    }

    /**
     * Color constraint getter
     * @return color constraint
     */
    public String getColorConstraint() {
        return colorConstraint;
    }

    /**
     * Row index getter
     * @return row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Column index getter
     * @return column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Number constraint getter
     * @return number constraint
     */
    public int getNumberConstraint() {
        return numberConstraint;
    }

    /**
     * Assigned die getter
     * @return assigned die
     */
    public Dice getAssignedDice() {
        return assignedDice;
    }

    /**
     * Neighbour cells getter
     * @return list of neighbour cells
     */
    public List<WindowCell> getNeighbourCells() {
        return neighbourCells;
    }

    /**
     * Diagonal cells getter
     * @return list of diagonal cells
     */
    public List<WindowCell> getDiagonalCells() { return diagonalCells; }

    /**
     * Assigned dice setter
     * @param assignedDice assigned die
     * @throws NotEmptyWindowCellException if the cell is already assigned with another dice
     */
    public void setAssignedDice(Dice assignedDice) throws NotEmptyWindowCellException{
        if (this.assignedDice == null) this.assignedDice = assignedDice;
        else throw new NotEmptyWindowCellException("Not empty");
    }

    /**
     * This method removes the assigned die from a window cell
     */
    public void removeDice(){
        this.assignedDice = null;
    }

    /**
     * Boolean is empty
     * @return true if the cell is not assigned with a dice
     */
    public boolean isEmpty(){
        return null == assignedDice;
    }

    /**
     * Once the grid is ready, this method assigns to the cell the list of its neighbour cells to make
     * controls easier
     * @param grid grid of window cells
     */
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

    /**
     * Once the grid is ready, this method assigns to the cell the list of its diagonals neighbour cells
     * to make controls easier
     * @param grid grid of window cells
     */
    public void setDiagonals(WindowCell[][] grid) {
        if (!isDiagonal) {
            this.isDiagonal = true;
            int x = this.row;
            int y = this.column;

            if(grid == null) return;
            if((x - 1 >= 0) && (y - 1 >= 0)) diagonalCells.add(grid[x-1][y-1]);
            if((x + 1 < 4) && (y + 1 < 5)) diagonalCells.add(grid[x+1][y+1]);
            if((x - 1 >= 0) && (y + 1 < 5)) diagonalCells.add(grid[x-1][y+1]);
            if((x + 1 < 4) && (y - 1 >= 0)) diagonalCells.add(grid[x+1][y-1]);
        }
    }

    /**
     * Equals method
     * @param o object to be compared
     * @return true if the condition is respected
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowCell that = (WindowCell) o;
        return row == that.row &&
                column == that.column &&
                numberConstraint == that.numberConstraint &&
                neighbourhoodAssigned == that.neighbourhoodAssigned &&
                isDiagonal == that.isDiagonal &&
                Objects.equals(colorConstraint, that.colorConstraint) &&
                Objects.equals(assignedDice, that.assignedDice) &&
                Objects.equals(neighbourCells, that.neighbourCells) &&
                Objects.equals(diagonalCells, that.diagonalCells);
    }

}

