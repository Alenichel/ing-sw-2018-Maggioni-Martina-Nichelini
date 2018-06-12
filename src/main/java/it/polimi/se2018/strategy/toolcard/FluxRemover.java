package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.DiceLocation;
import it.polimi.se2018.utils.ToolCardsName;

import java.io.Serializable;

/**
 * This class implements Tool Card #11 "Flux Remover" which lets the player return a die from the
 * draft pool to the bag and pull a new one from the bag.
 * The player gets to choose its number and places it on his window pattern card obeying all
 * restrictions or returns it to the draft pool.
 */
public class FluxRemover implements ToolCardEffectStrategy, Serializable {

    private WindowPatternCard wpc;
    private WindowCell wc;
    private int number;
    private Dice d1;
    private Dice d2;

    public FluxRemover(){
        }

    public FluxRemover FluxRemover(FluxRemover fluxRemover, Dice d1, Dice d2, WindowPatternCard wpc, int number, WindowCell wc) {
        fluxRemover.wpc = wpc;
        fluxRemover.wc = wc;
        fluxRemover.number = number;
        fluxRemover.d1 = d1;
        fluxRemover.d2 = d2;
        return fluxRemover;
    }

    @Override
    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException{

        if(!d1.getLocation().equals(DiceLocation.TABLE)) {
            throw new ToolCardException("die can not be chosen");
        }
        else {
            d1.setLocation(DiceLocation.BAG);
        }

        if(!d2.getLocation().equals(DiceLocation.BAG)){
            throw new ToolCardException("die can not be chosen 2");
        }

        else{
            if (number > 0 &&  number < 7) {
                d2.setNumber(number);
            }
            else throw new ToolCardException("invalid number");


            this.wpc.insertDice(d2, wc.getRow(), wc.getColumn(), true, true, true);

        }

        return 0;
    }
}