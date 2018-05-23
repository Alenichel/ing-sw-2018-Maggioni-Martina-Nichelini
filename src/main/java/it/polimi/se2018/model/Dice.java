package it.polimi.se2018.model;


import it.polimi.se2018.utils.DiceLocation;

import java.util.Objects;
import java.util.Random;

public class Dice {
    private int number;
    private String color;
    private DiceLocation location;
    private String unicode = "";

    public Dice(String color){
        this.color = color;
        this.location = null;
        this.rollDice();
        this.toUnicode(this.color, this.getNumber());
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

    public DiceLocation getLocation() {
        return location;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLocation(DiceLocation location) {
        this.location = location;
    }

    public void rollDice(){
        Random rand = new Random();
        this.number =rand.nextInt(4)+1;
        this.toUnicode(this.color, this.number);
    }

    private void toUnicode(String color, int n){
        this.unicode = "";
        this.unicode = this.unicode.concat(toUnicodeColor(color));
        this.unicode = this.unicode.concat(toUnicodeNumber(n));
        this.unicode = this.unicode.concat((char) 27 + "[30m");
    }

    private String toUnicodeColor(String color){
        switch (color.toLowerCase()){
            case "red":         return (char) 27 + "[31m";
            case "yellow":      return (char) 27 + "[33m";
            case "green":       return (char) 27 + "[32m";
            case "blue":        return (char) 27 + "[34m";
            case "purple":      return (char) 27 + "[35m";
            default: return "";
        }
    }

    private String toUnicodeNumber(int number){
        switch (number){
            case 1:    return "\u2680";
            case 2:    return "\u2681";
            case 3:    return "\u2682";
            case 4:    return "\u2683";
            case 5:    return "\u2684";
            case 6:    return "\u2685";
            default: return "";
        }
    }

    public String toString(){
        return this.unicode;
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

