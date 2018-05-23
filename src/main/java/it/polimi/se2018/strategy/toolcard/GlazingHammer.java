package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.util.ArrayList;

public class GlazingHammer implements ToolCardEffectStrategy {
    private ArrayList<Dice> draftedDice;
    public GlazingHammer(){
    }
    public GlazingHammer GlazingHammer( GlazingHammer glazingHammer, ArrayList<Dice> draftedDice){
        glazingHammer.draftedDice = draftedDice;
        return glazingHammer;
    }

    @Override
    public int executeEffect() {
        for(Dice dice : this.draftedDice){
            dice.rollDice();
        }
        return 1;
    }
}
