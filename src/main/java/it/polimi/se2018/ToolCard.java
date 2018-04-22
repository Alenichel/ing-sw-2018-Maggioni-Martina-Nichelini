package it.polimi.se2018;

public class ToolCard extends Card{
    private String diceColor;
    private String description;
    private boolean used;
    private ToolCardEffectStrategy toolCardEffect;
    private Game gameReference;

    public void toolCard(Game game){

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

    public void executeEffect(){
        
    }

}
