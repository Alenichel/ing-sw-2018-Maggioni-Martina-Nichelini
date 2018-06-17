package it.polimi.se2018.model;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class ToolCard extends Card implements Serializable {
    private DiceColor diceColor;
    private String description;
    private boolean used;
    private Game gameReference;
    private ToolCardsName toolCardName;
    private ToolcardContent[] content;

    public ToolCard(ToolCardsName tcn){
        /*if(ToolCardsName.GrozingPliers.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie, ToolcardContent.Increase    };

            this.setName(ToolCardsName.GrozingPliers.toString());
            this.setToolCardName(ToolCardsName.GrozingPliers);
            this.setDescription("After drafting increase or decrease the value of the drafted die by 1.");
        }
        else */if(ToolCardsName.EnglomiseBrush.equals(tcn)){
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
        /*
        else if(ToolCardsName.Lathekin.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.WindowCellStart, ToolcardContent.WindowCellEnd, ToolcardContent.WindowCellStart, ToolcardContent.WindowCellEnd};

            this.setName(ToolCardsName.Lathekin.toString());
            this.setToolCardName(ToolCardsName.Lathekin);
            this.setDescription("Move exactly two dice, obeying all placement restrictions.");
        }
        else if(ToolCardsName.LensCutter.equals(tcn)){
            content = new ToolcardContent[] {   ToolcardContent.RoundTrackDie, ToolcardContent.DraftedDie   };

            this.setName(ToolCardsName.LensCutter.toString());
            this.setToolCardName(ToolCardsName.LensCutter);
            this.setDescription("After drafted swap the drafted die with a die from the round track.");
        }
        else if(ToolCardsName.FluxBrush.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie, ToolcardContent.WindowCellEnd  };

            this.setName(ToolCardsName.FluxBrush.toString());
            this.setToolCardName(ToolCardsName.FluxBrush);
            this.setDescription("After drafting re roll the drafted die. If it cannot be placed, return it to the drafted pool.");
        }
        else if(ToolCardsName.GlazingHammer.equals(tcn)){
            this.setName(ToolCardsName.GlazingHammer.toString());
            this.setToolCardName(ToolCardsName.GlazingHammer);
            this.setDescription("Reroll all dice in the drafted pool. This may only used on your second turn before drafting.");
        }/*
        else if(ToolCardsName.RunningPliers.equals(tcn)){
            this.setName(ToolCardsName.RunningPliers.toString());
            this.setToolCardName(ToolCardsName.RunningPliers);
            this.setDescription("After your first turn, immediately draft a die. Skip your next turn this round.");
        }*/
        else if(ToolCardsName.CorkBackedStraightedge.equals(tcn)){
            content = new ToolcardContent[]{ToolcardContent.RunBy, ToolcardContent.DraftedDie, ToolcardContent.WindowCellEnd  };

            this.setName(ToolCardsName.CorkBackedStraightedge.toString());
            this.setToolCardName(ToolCardsName.CorkBackedStraightedge);
            this.setDescription("After drafting, place the die in a spot that is not adjacent to another die. You must obey all other placement restriction.");
        }/*
        else if(ToolCardsName.GrindingStone.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie  };

            this.setName(ToolCardsName.GrindingStone.toString());
            this.setToolCardName(ToolCardsName.GrindingStone);
            this.setDescription("After drafting flip the die to its opposite side.");
        }
        else if(ToolCardsName.FluxRemover.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.DraftedDie    };

            this.setName(ToolCardsName.FluxRemover.toString());
            this.setToolCardName(ToolCardsName.FluxRemover);
            this.setDescription("After drafting return the die to the dice bag and pull 1 die from the bag. Choose a value and place the new die, obeying all placement restriction or return to the dice bag.");
        }
        else if(ToolCardsName.TapWheel.equals(tcn)){
            content = new ToolcardContent[]{    ToolcardContent.WindowCellStart, ToolcardContent.WindowCellStart, ToolcardContent.WindowCellStart, ToolcardContent.WindowCellStart    };
            this.setName(ToolCardsName.TapWheel.toString());
            this.setToolCardName(ToolCardsName.TapWheel);
            this.setDescription("Move up to two dice of the same color that match the color of a die in the round track. You must obey all the placement restriction.");
        }*/
    }

    public void setUsed(boolean value){
        used = value;
    }
    public void setDiceColor(DiceColor diceColor) {
        this.diceColor = diceColor;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGameReference(Game gameReference) {
        this.gameReference = gameReference;
    }
    public void setName(String name){
        this.name = name;
    }

    private void setToolCardName(ToolCardsName toolCardName) {
        this.toolCardName = toolCardName;
    }

    public DiceColor getDiceColor() {
        return diceColor;
    }
    public boolean isUsed() {
        return used;
    }

    public Game getGameReference() {
        return gameReference;
    }

    public String getDescription(){
        return description;
    }
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }

    public String getName() {
        return name;
    }

    public ToolcardContent[] getContent() {
        return content;
    }

    public String toString(){
        String str ="";
        str = str.concat(this.name + "  " + (char) 27 + "[30m");
        str = str.concat("\""+ this.description + "\"");
        if(isUsed()){
            str = str.concat(" it costs : 2 \n");
        }else {
            str = str.concat(" it costs : 1 \n");
        }

        return str;
    }

}
