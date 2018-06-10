package it.polimi.se2018.model;


import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.DiceLocation;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Dice implements Serializable {
    private int number;
    private String color;
    private DiceLocation location;
    private String unicode = "";
    public Dice(){}

    public Dice (DiceColor color){
        this.color = color.name();
        this.location = null;
        this.rollDice();
        this.toUnicode(this.color, this.getNumber());
    }

    public Dice(DiceColor color, int number){
        this.color = color.name();
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
        this.number =rand.nextInt(6)+1;
        this.toUnicode(this.color, this.number);
    }

    private void toUnicode(String color, int n){
        ConsoleUtils consoleUtils = new ConsoleUtils();
        this.unicode = "";
        this.unicode = this.unicode.concat(consoleUtils.toUnicodeColor(color));
        this.unicode = this.unicode.concat(consoleUtils.toUnicodeNumber(n));
        this.unicode = this.unicode.concat((char) 27 + "[30m");
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

