package it.polimi.se2018;

import java.awt.*;

public class WindowCell {

    private int row;
    private int column;
    private Color colorConstraint;
    private String numberConstraint;
    private Dice assignedDice;


    public WindowCell(Color colorConstraint, String numberConstraint ) {
        if (colorConstraint == null){
            this.numberConstraint = numberConstraint;
        } else {
            this.colorConstraint = colorConstraint;
        }
    }

    public Color getColorConstraint() {
        return colorConstraint;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getNumberConstraint() {
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

