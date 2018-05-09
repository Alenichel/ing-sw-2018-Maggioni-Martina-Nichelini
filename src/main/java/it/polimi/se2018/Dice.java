package it.polimi.se2018;


import java.awt.*;
import java.util.Random;

public class Dice {
    private int id;
    private int number;
    private String color;
    private String location;

    public Dice(int number, String color, String location){
        //Dice constructor
        this.id = this.hashCode();
        this.number = number;
        this.color = color;
        this.location = location;

    }

    public String getColor() {
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
        Random rand = new Random();
        this.number =rand.nextInt(4)+1;
    }

    public String toString(){
        String string ="**  ";
        string += this.number;
        string += " ";
        string += this.color;
        string += " ";
        string += this.location;
        string += "  **";
        return string;
    }

}

