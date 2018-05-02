package it.polimi.se2018;

public class Dice {

    private int id;
    private int number;
    private String color;
    private String location;

    public Dice(){
        //Dice constructor
    }

    public String getColor() {
        return color;
    }

    public int getNumberber() {
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

