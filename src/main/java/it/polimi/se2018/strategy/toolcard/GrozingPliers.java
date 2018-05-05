package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.ToolCardEffectStrategy;

public class GrozingPliers implements ToolCardEffectStrategy {

    Dice draftedDice;
    boolean increase;

    public GrozingPliers(Dice draftedDice, boolean increase) {
        this.draftedDice = draftedDice;
        this.increase = increase;
    }

    public GrozingPliers(GrozingPliers grozingPliers, Dice draftedDice, boolean increase){

        //Non devo riferirmi all'oggetto grozingPliers?????

        this.draftedDice = draftedDice;
        this.increase = increase;
    }



    public int executeEffect() {
        if (draftedDice.getNumber() != 6 || draftedDice.getNumber() != 1) {
            if (increase) {
                draftedDice.setNumber(draftedDice.getNumber() + 1);
            } else {
                draftedDice.setNumber(draftedDice.getNumber() - 1);
            }
        }
        return 1;
    }
}