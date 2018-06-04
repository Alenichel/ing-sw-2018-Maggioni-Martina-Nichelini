package it.polimi.se2018.model;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.strategy.toolcard.*;
import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.Arrays;

public class ToolCard extends Card implements Serializable {
    private DiceColor diceColor;
    private String description;
    private boolean used;
    private ToolCardEffectStrategy toolCardEffect;
    private Game gameReference;

    public ToolCard(){}
    public ToolCard(ToolCardEffectStrategy toolCardEffect){
        this.toolCardEffect = toolCardEffect;

        if(toolCardEffect instanceof CopperFoilBurnisher){
            this.setName(ToolCardsName.CopperFoilBurnisher.toString());
            this.setDescription("Move any one die in your windows ingnoring shade restriction. You must obey all other placement restriction");
        }
        if(toolCardEffect instanceof CorkBackedStraightedge){
            this.setName(ToolCardsName.CorkBackedStraightedge.toString());
            this.setDescription("After drafting, place the die in a spot that is not adjacent to another die. You must obey all other placement restriction.");
        }
        if(toolCardEffect instanceof EnglomiseBrush){
            this.setName(ToolCardsName.EnglomiseBrush.toString());
            this.setDescription("Move any one die in your windows ignoring the color restriction. You must obey all other placement restriction");
        }
        if(toolCardEffect instanceof FluxBrush){
            this.setName(ToolCardsName.FluxBrush.toString());
            this.setDescription("After drafting re roll the drafted die. If it cannot be placed, return it to the drafted pool");
        }
        if(toolCardEffect instanceof FluxRemover){
            this.setName(ToolCardsName.FluxRemover.toString());
            this.setDescription("After drafting return the die to the dice bag and pull 1 die from the bag. Choose a value and place the new die, obeying all placement restriction or return to the dice bag ");
        }
        if(toolCardEffect instanceof GlazingHammer){
            this.setName(ToolCardsName.GlazingHammer.toString());
            this.setDescription("Re roll all dice in the drafted pool. This may only used on your second turn before drafting.");
        }
        if(toolCardEffect instanceof GrindingStone){
            this.setName(ToolCardsName.GrindingStone.toString());
            this.setDescription("After drafting flip the die to its opposite side.");
        }
        if(toolCardEffect instanceof GrozingPliers){
            this.setName(ToolCardsName.GrozingPliers.toString());
            this.setDescription("After drafting increase or decrease the value of the drafted die by 1.");
        }
        if(toolCardEffect instanceof Lathekin){
            this.setName(ToolCardsName.Lathekin.toString());
            this.setDescription("Move exatcly two die, obeying all placemente restriction");
        }
        if(toolCardEffect instanceof LensCutter){
            this.setName(ToolCardsName.LensCutter.toString());
            this.setDescription("After drafted swap the drafted die with a die from the round track");
        }
        if(toolCardEffect instanceof RunningPliers){
            this.setName(ToolCardsName.RunningPliers.toString());
            this.setDescription("After your first turn, immediately draft a die. Skip your next turn this round");
        }
        if(toolCardEffect instanceof TapWheel){
            this.setName(ToolCardsName.TapWheel.toString());
            this.setDescription("Move up to two dice of the same color that match the color of a die in the round track. You must obey all the placement restriction");
        }
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
    public void setToolCardEffect(ToolCardEffectStrategy toolCardEffect) {
        this.toolCardEffect = toolCardEffect;
    }
    public void setGameReference(Game gameReference) {
        this.gameReference = gameReference;
    }
    public void setName(String name){
        this.name = name;
    }

    public DiceColor getDiceColor() {
        return diceColor;
    }
    public boolean isUsed() {
        return used;
    }
    public ToolCardEffectStrategy getToolCardEffect() {
        return toolCardEffect;
    }
    public Game getGameReference() {
        return gameReference;
    }
    public boolean getUsed(){
        return used;
    }
    public String getDescription(){
        return description;
    }

    public int executeEffect() throws ToolCardException, NotEmptyWindowCellException{
        try{
            return toolCardEffect.executeEffect();
        }catch (ToolCardException | NotEmptyWindowCellException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            return 0;
        }
    }

    public String toString(){
        String str ="";
        ConsoleUtils c = new ConsoleUtils();
        //str = str.concat(c.toUnicodeColor(this.diceColor.toString()));
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
