package it.polimi.se2018;

import it.polimi.se2018.Exception.ToolCardException;

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

    public boolean getUsed(){
        return used;
    }

    public String getDescription(){
        return description;
    }

    public int executeEffect(){
        try{
            return toolCardEffect.executeEffect();
        }catch (ToolCardException e){
            e.printStackTrace();
            return 0;
        }
    }

}
