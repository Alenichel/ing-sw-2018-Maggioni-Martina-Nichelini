package it.polimi.se2018;

import java.awt.*;

public class WindowCell {
    //----servono?----
    private int row;
    private int column;
    //----------------
    private String colorConstraint;
    private int numberConstraint;
    private Dice assignedDice;


    public WindowCell(){
    }

    public WindowCell(int numberConstraint) {
        this.numberConstraint = numberConstraint;
        this.colorConstraint = null;
    }

    public WindowCell(String colorConstraint){
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

