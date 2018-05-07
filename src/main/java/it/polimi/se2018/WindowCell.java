package it.polimi.se2018;

public class WindowCell {

    private int row;
    private int column;
    private String colorConstraint;
    private int numberConstraint;
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

    public Dice getAssignedDice() {
        return assignedDice;
    }

    public void setAssignedDice(Dice assignedDice) {
        this.assignedDice = assignedDice;
    }

    public boolean isEmpty(){
        return null == assignedDice;
    }
}

