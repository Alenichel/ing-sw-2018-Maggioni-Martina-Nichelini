package it.polimi.se2018;

import java.awt.*;

public class Dice {

    private int id;
    private int number;
    private Color color;
    private String location;

    public Dice(int number, Color color, String location){
        //Dice constructor
        this.id = this.hashCode();
        this.number = number;
        this.color = color;
        this.location = location;

    }

    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void rollDice(){
        //rolldice
    }

}

