package it.polimi.se2018.strategy.toolcard;


import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.DiceLocation;

public class LensCutter implements ToolCardEffectStrategy {

    private Dice draftedDice;
    private Dice rtDice;

    public LensCutter(){ }

    public LensCutter LensCutter(LensCutter lensCutter, Dice draftedDice) {
        lensCutter.draftedDice = draftedDice;
        lensCutter.rtDice = rtDice;
        return lensCutter;
    }

    @Override
    public int executeEffect(){
        rtDice.setLocation(DiceLocation.WINDOWPATTERNCARD); //scelta dal giocatore
        draftedDice.setLocation(DiceLocation.ROUNDTRACK); //scelta dal giocatore
        return 0;
    }
}
