package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

public class GrozingPliers implements ToolCardEffectStrategy {

    private Dice draftedDice;
    private boolean increase;

    public GrozingPliers() { }

    public GrozingPliers GrozingPliers(GrozingPliers grozingPliers, Dice draftedDice, boolean increase){
        grozingPliers.draftedDice = draftedDice;
        grozingPliers.increase = increase;
        return grozingPliers;
    }


    @Override
    public int executeEffect() {
        if (increase && draftedDice.getNumber() != 6) {
            draftedDice.setNumber(draftedDice.getNumber() + 1);
        } else if (draftedDice.getNumber() != 1) {
            draftedDice.setNumber(draftedDice.getNumber() - 1);
            }
        return 1;
        }
}