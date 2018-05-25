package it.polimi.se2018.model;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;

public class ToolCard extends Card{
    private String diceColor;
    private String description;
    private boolean used;
    private ToolCardEffectStrategy toolCardEffect;
    private Game gameReference;

    public ToolCard(ToolCardEffectStrategy toolCardEffect){
        this.toolCardEffect = toolCardEffect;
    }

    public void setUsed(boolean value){
        used = value;
    }
    public void setDiceColor(String diceColor) {
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

    public String getDiceColor() {
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

    public int executeEffect(){
        try{
            return toolCardEffect.executeEffect();
        }catch (ToolCardException | NotEmptyWindowCellException e){
            e.printStackTrace();
            return 0;
        }
    }

}
