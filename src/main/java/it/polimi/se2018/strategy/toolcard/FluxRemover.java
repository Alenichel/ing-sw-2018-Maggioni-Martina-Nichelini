package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.ToolCardEffectStrategy;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #11 "Flux Remover" which lets the player return a die from the
 * draft pool to the bag and pull a new one from the bag.
 * The player gets to choose its number and places it on his window pattern card obeying all
 * restrictions or returns it to the draft pool.
 */
public class FluxRemover extends ToolCard implements ToolCardEffectStrategy, Serializable {

    public FluxRemover(){
        this.setName(ToolCardsName.FluxBrush.toString());
        this.setDescription("After drafting return the die to the dice bag and pull 1 die from the bag. Choose a value and place the new die, obeying all placement restriction or return to the dice bag ");
    }

    public FluxRemover FluxRemover(FluxRemover fluxRemover) {
        return fluxRemover;
    }

    @Override
    public int executeEffect(){
        return 0;
    }
}