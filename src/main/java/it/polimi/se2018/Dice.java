package it.polimi.se2018;


import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Dice {
    private int number;
    private String color;
    private String location;

    public Dice(String color){
        this.color = color;
        this.location = null;
        this.rollDice();
    }

    public Dice(String color, int number){
        this.color = color;
        this.location = null;
        this.number = number;
    }

    public String getColor() {
        return this.color;
    }

    public int getNumber() {
        return this.number;
    }

    public String getLocation() {
        return location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dice dice = (Dice) o;
        return number == dice.number &&
                Objects.equals(color, dice.color) &&
                Objects.equals(location, dice.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(number, color, location);
    }
}

