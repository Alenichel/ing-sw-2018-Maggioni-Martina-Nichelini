package it.polimi.se2018;

public class WindowCell {

    private int row;
    private int column;
    private String colorConstraint;
    private String numberContstraint;
    private Dice assignedDice;

    public WindowCell() {

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
}

