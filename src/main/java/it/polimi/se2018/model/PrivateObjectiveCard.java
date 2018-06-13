package it.polimi.se2018.model;

import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.utils.DiceColor;

public class PrivateObjectiveCard extends ObjectiveCard {

    DiceColor color;

    public DiceColor getColor(){
        return this.color;
    }

    public PrivateObjectiveCard(DiceColor diceColor){
        this.color = diceColor;
    }

    @Override
    public String toString() {
        ConsoleUtils c = new ConsoleUtils();
        String str ="";
        str = str.concat("Your private objective: ");
        str = str.concat(c.toUnicodeColor(this.color.toString()) + "\u25FE" + (char) 27 + "[30m");

        return str;
    }
}
