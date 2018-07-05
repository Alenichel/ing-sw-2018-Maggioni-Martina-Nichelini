package it.polimi.se2018.model;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class implements the model side of the tool cards
 */
public class ToolCard extends Card implements Serializable {
    private DiceColor diceColor;
    private String description;
    private boolean used;
    private Game gameReference;
    private ToolCardsName toolCardName;
    private ToolcardContent[] content;

    /**
     * ToolCard constructor
     * @param tcn tool cards name
     */
    public ToolCard(ToolCardsName tcn){
        if(ToolCardsName.GrozingPliers.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie, ToolcardContent.Increase    };

            this.setName(ToolCardsName.GrozingPliers.toString());
            this.setToolCardName(ToolCardsName.GrozingPliers);
            this.setDescription("After drafting increase or decrease the value of the drafted die by 1.");
        }
        else if(ToolCardsName.EnglomiseBrush.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.RunBy, ToolcardContent.WindowCellStart, ToolcardContent.WindowCellEnd   };

            this.setName(ToolCardsName.EnglomiseBrush.toString());
            this.setToolCardName(ToolCardsName.EnglomiseBrush);
            this.setDescription("Move any one die in your windows ignoring the color restriction. You must obey all other placement restriction.");
        }
        else if(ToolCardsName.CopperFoilBurnisher.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.RunBy, ToolcardContent.WindowCellStart, ToolcardContent.WindowCellEnd   };

            this.setName(ToolCardsName.CopperFoilBurnisher.toString());
            this.setToolCardName(ToolCardsName.CopperFoilBurnisher);
            this.setDescription("Move any one die in your windows ignoring shade restriction. You must obey all other placement restriction.");
        }

        else if(ToolCardsName.Lathekin.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.RunBy, ToolcardContent.firstWindowCellStart, ToolcardContent.firstWindowCellEnd, ToolcardContent.secondWindowCellStart, ToolcardContent.secondWindowCellEnd};

            this.setName(ToolCardsName.Lathekin.toString());
            this.setToolCardName(ToolCardsName.Lathekin);
            this.setDescription("Move exactly two dice, obeying all placement restrictions.");
        }
        else if(ToolCardsName.LensCutter.equals(tcn)){
            content = new ToolcardContent[] {    ToolcardContent.DraftedDie, ToolcardContent.RoundTrackDie   };

            this.setName(ToolCardsName.LensCutter.toString());
            this.setToolCardName(ToolCardsName.LensCutter);
            this.setDescription("After drafted swap the drafted die with a die from the round track.");
        }
        else if(ToolCardsName.FluxBrush.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.RunBy, ToolcardContent.DraftedDie, ToolcardContent.WindowCellEnd  };

            this.setName(ToolCardsName.FluxBrush.toString());
            this.setToolCardName(ToolCardsName.FluxBrush);
            this.setDescription("After drafting re roll the drafted die. If it cannot be placed, return it to the drafted pool.");
        }
        else if(ToolCardsName.GlazingHammer.equals(tcn)){
            this.setName(ToolCardsName.GlazingHammer.toString());
            this.setToolCardName(ToolCardsName.GlazingHammer);
            this.setDescription("Reroll all dice in the drafted pool. This may only used on your second turn before drafting.");
        }
        else if(ToolCardsName.RunningPliers.equals(tcn)){
            content = new ToolcardContent[]{  ToolcardContent.RunBy  };
            this.setName(ToolCardsName.RunningPliers.toString());
            this.setToolCardName(ToolCardsName.RunningPliers);
            this.setDescription("After your first turn, immediately draft a die. Skip your next turn this round.");
        }
        else if(ToolCardsName.CorkBackedStraightedge.equals(tcn)){
            content = new ToolcardContent[]{ToolcardContent.RunBy, ToolcardContent.DraftedDie, ToolcardContent.WindowCellEnd  };

            this.setName(ToolCardsName.CorkBackedStraightedge.toString());
            this.setToolCardName(ToolCardsName.CorkBackedStraightedge);
            this.setDescription("After drafting, place the die in a spot that is not adjacent to another die. You must obey all other placement restriction.");
        }
        else if(ToolCardsName.GrindingStone.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie  };

            this.setName(ToolCardsName.GrindingStone.toString());
            this.setToolCardName(ToolCardsName.GrindingStone);
            this.setDescription("After drafting flip the die to its opposite side.");
        }
        else if(ToolCardsName.FluxRemover.equals(tcn)){
            content = new ToolcardContent[]{   ToolcardContent.RunBy,  ToolcardContent.DraftedDie, ToolcardContent.BagDie , ToolcardContent.Number,  ToolcardContent.WindowCellEnd  };

            this.setName(ToolCardsName.FluxRemover.toString());
            this.setToolCardName(ToolCardsName.FluxRemover);
            this.setDescription("After drafting return the die to the dice bag and pull 1 die from the bag. Choose a value and place the new die, obeying all placement restriction or return to the dice bag.");
        }
        else if(ToolCardsName.TapWheel.equals(tcn)){
            content = new ToolcardContent[]{  ToolcardContent.RunBy, ToolcardContent.Amount, ToolcardContent.firstWindowCellStart, ToolcardContent.firstWindowCellEnd, ToolcardContent.secondWindowCellStart, ToolcardContent.secondWindowCellEnd};
            this.setName(ToolCardsName.TapWheel.toString());
            this.setToolCardName(ToolCardsName.TapWheel);
            this.setDescription("Move up to two dice of the same color that match the color of a die in the round track. You must obey all the placement restriction.");
        }
    }

    /**
     * Used setter
     * @param value true if it's used, false otherwise
     */
    public void setUsed(boolean value){
        used = value;
    }

    /**
     * Die color setter
     * @param diceColor: red, purple, blue, green or yellow
     */
    public void setDiceColor(DiceColor diceColor) {
        this.diceColor = diceColor;
    }

    /**
     * Description setter
     * @param description of the tool card
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Game reference setter
     * @param gameReference reference to the game
     */
    public void setGameReference(Game gameReference) {
        this.gameReference = gameReference;
    }

    /**
     * Name setter
     * @param name name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Tool card name setter
     * @param toolCardName tool card name
     */
    private void setToolCardName(ToolCardsName toolCardName) {
        this.toolCardName = toolCardName;
    }

    /**
     * Die color getter
     * @return dice color
     */
    public DiceColor getDiceColor() {
        return diceColor;
    }

    /**
     * Boolean is used
     * @return true if it's used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Game reference getter
     * @return game reference
     */
    public Game getGameReference() {
        return gameReference;
    }

    /**
     * Description getter
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Tool card name getter
     * @return tool card name
     */
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }

    /**
     * Name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Tool card content getter
     * @return Tool card content
     */
    public ToolcardContent[] getContent() {
        return content;
    }

    /**
     * To string method
     * @return string
     */
    public String toString(){
        String str ="";
        str = str.concat(this.name + "  " + (char) 27 + "[30m");
        str = str.concat("\""+ this.description + "\"");
        if(isUsed()){
            str = str.concat("|| Cost: 2 \n");
        }else {
            str = str.concat("|| Cost: 1 \n");
        }

        return str;
    }

}
