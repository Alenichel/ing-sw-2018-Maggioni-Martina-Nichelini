package it.polimi.se2018.model;

import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.Arrays;

public class ToolCard extends Card implements Serializable {
    private DiceColor diceColor;
    private String description;
    private boolean used;
    private ToolCardEffectStrategy toolCardEffect;
    private Game gameReference;
    private ToolCardsName toolCardName;

    public ToolCard(){}
    public ToolCard(ToolCardsName tcn){
        this.toolCardEffect = toolCardEffect;

        if(ToolCardsName.GrozingPliers.equals(tcn)){
            this.setName(ToolCardsName.GrozingPliers.toString());
            this.setToolCardName(ToolCardsName.GrozingPliers);
            this.setDescription("After drafting increase or decrease the value of the drafted die by 1.");
        }
        if(ToolCardsName.EnglomiseBrush.equals(tcn)){
            this.setName(ToolCardsName.EnglomiseBrush.toString());
            this.setToolCardName(ToolCardsName.EnglomiseBrush);
            this.setDescription("Move any one die in your windows ignoring the color restriction. You must obey all other placement restriction.");
        }
        if(ToolCardsName.CopperFoilBurnisher.equals(tcn)){
            this.setName(ToolCardsName.CopperFoilBurnisher.toString());
            this.setToolCardName(ToolCardsName.CopperFoilBurnisher);
            this.setDescription("Move any one die in your windows ignoring shade restriction. You must obey all other placement restriction.");
        }
        /*if(ToolCardsName.Lathekin.equals(tcn)){
            this.setName(ToolCardsName.Lathekin.toString());
            this.setToolCardName(ToolCardsName.Lathekin);
            this.setDescription("Move exactly two dice, obeying all placement restrictions.");
        }
        if(ToolCardsName.LensCutter.equals(tcn)){
            this.setName(ToolCardsName.LensCutter.toString());
            this.setToolCardName(ToolCardsName.LensCutter);
            this.setDescription("After drafted swap the drafted die with a die from the round track.");
        }
        if(ToolCardsName.FluxBrush.equals(tcn)){
            this.setName(ToolCardsName.FluxBrush.toString());
            this.setToolCardName(ToolCardsName.FluxBrush);
            this.setDescription("After drafting re roll the drafted die. If it cannot be placed, return it to the drafted pool.");
        }
        if(ToolCardsName.GlazingHammer.equals(tcn)){
            this.setName(ToolCardsName.GlazingHammer.toString());
            this.setToolCardName(ToolCardsName.GlazingHammer);
            this.setDescription("Re roll all dice in the drafted pool. This may only used on your second turn before drafting.");
        }
        if(ToolCardsName.RunningPliers.equals(tcn)){
            this.setName(ToolCardsName.RunningPliers.toString());
            this.setToolCardName(ToolCardsName.RunningPliers);
            this.setDescription("After your first turn, immediately draft a die. Skip your next turn this round.");
        }
        if(ToolCardsName.CorkBackedStraightedge.equals(tcn)){
            this.setName(ToolCardsName.CorkBackedStraightedge.toString());
            this.setToolCardName(ToolCardsName.CorkBackedStraightedge);
            this.setDescription("After drafting, place the die in a spot that is not adjacent to another die. You must obey all other placement restriction.");
        }
        if(ToolCardsName.GrindingStone.equals(tcn)){
            this.setName(ToolCardsName.GrindingStone.toString());
            this.setToolCardName(ToolCardsName.GrindingStone);
            this.setDescription("After drafting flip the die to its opposite side.");
        }
        if(ToolCardsName.FluxRemover.equals(tcn)){
            this.setName(ToolCardsName.FluxRemover.toString());
            this.setToolCardName(ToolCardsName.FluxRemover);
            this.setDescription("After drafting return the die to the dice bag and pull 1 die from the bag. Choose a value and place the new die, obeying all placement restriction or return to the dice bag.");
        }
        if(ToolCardsName.TapWheel.equals(tcn)){
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
    public void setToolCardEffect(ToolCardEffectStrategy toolCardEffect) {
        this.toolCardEffect = toolCardEffect;
    }
    public void setGameReference(Game gameReference) {
        this.gameReference = gameReference;
    }
    public void setName(String name){
        this.name = name;
    }
    public  void setToolCardName(ToolCardsName toolCardName) {
        this.toolCardName = toolCardName;
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
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }
    public String getName() {
        return name;
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
