package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;

import java.io.Serializable;

public class RunningPliers implements ToolCardEffectStrategy, Serializable {

    private Dice draftedDice;

    public RunningPliers(){ }

    public RunningPliers RunningPliers (RunningPliers runningPliers, Dice draftedDice) {
        runningPliers.draftedDice = draftedDice;
        return runningPliers;
    }

    @Override
    public int executeEffect(){
        //subito dopo il primo turno del giocatore
        //giocatore sceglie un draftedDice
        //giocatore salta il suo secondo turno
        return 0;
    }

}
