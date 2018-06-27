package it.polimi.se2018.model;


import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.DiceLocation;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * This class implements all the dice of the game.
 */
public class Dice implements Serializable {
    private int number;
    private String color;
    private DiceLocation location;
    private DiceColor diceColor;
    private String unicode = "";
    public Dice(){}

    /**
     * Dice's first constructor.
     * @param color which can be red, yellow, purple, blue or green
     */
    public Dice (DiceColor color){
        this.diceColor = color;
        this.color = color.name();
        this.location = null;
        this.rollDice();
    }

    /**
     * Dice's second constructor.
     * @param color which can be red, yellow, purple, blue or green
     * @param number 1, 2, 3, 4, 5 or 6
     */
    public Dice(DiceColor color, int number){
        this.color = color.name();
        this.location = null;
        this.number = number;
    }

    /**
     * Color getter
     * @return color (red, purple, yellow, blue or green)
     */
    public String getColor() {
        return this.color;
    }

    public DiceColor getDiceColor() {
        return diceColor;
    }

    /**
     * Number getter
     * @return number (1, 2, 3, 4, 5 or 6)
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Location getter
     * @return location (bag, table, roundtrack or windowcell)
     */
    public DiceLocation getLocation() {
        return location;
    }

    /**
     * Number setter
     * @param number (1, 2, 3, 4, 5 or 6)
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Location setter
     * @param location (bag, table, roundtrack or windowcell)
     */
    public void setLocation(DiceLocation location) {
        this.location = location;
    }

    /**
     * This method allows you to roll a die (keep its color but change its number)
     */
    public void rollDice(){
        Random rand = new Random();
        this.number =rand.nextInt(6)+1;
    }

    private void toUnicode(String color, int n){
        ConsoleUtils consoleUtils = new ConsoleUtils();
        this.unicode = "";
        this.unicode = this.unicode.concat(consoleUtils.toUnicodeColor(color));
        this.unicode = this.unicode.concat(consoleUtils.toUnicodeNumber(n));
        this.unicode = this.unicode.concat((char) 27 + "[30m");
    }

    public String toString(){
        this.toUnicode(this.color, this.number);
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

