package it.polimi.se2018.strategy.toolcard;


import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.DiceLocation;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #5 "Lens Cutter" which lets the player swap a drafted die with
 * one on the round track
 */
public class LensCutter implements ToolCardEffectStrategy, Serializable {

    private Dice draftedDice;
    private Dice rtDice;

    public LensCutter(){
   }

    public LensCutter LensCutter(LensCutter lensCutter, Dice draftedDice, Dice rtDice) {
        lensCutter.draftedDice = draftedDice;
        lensCutter.rtDice = rtDice;
        return lensCutter;
    }

    @Override
    public int executeEffect() throws ToolCardException{
        DiceLocation rtLocation = rtDice.getLocation(); //posizione nel round track

        if (!rtDice.getLocation().equals(DiceLocation.ROUNDTRACK)) {
            throw new ToolCardException("die can not be chosen");
        }

        else {rtDice.setLocation(DiceLocation.TABLE);}


        if (!draftedDice.getLocation().equals(DiceLocation.TABLE)) {
            throw new ToolCardException("die can not be chosen");
        }
        draftedDice.setLocation(rtLocation);
        return 0;
    }
}
