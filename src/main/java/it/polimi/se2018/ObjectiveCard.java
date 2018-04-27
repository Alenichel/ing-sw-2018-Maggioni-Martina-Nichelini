package it.polimi.se2018;

public class ObjectiveCard extends Card{
    private String description;
    private Game gameReference;

    public ObjectiveCard (){
        this.gameReference = null;
    }
    public ObjectiveCard (Game game){
        this.gameReference = game;
    }

    public String getDescription() {
        return description;
    }

    public int scorePoint(){
        return 0;
    }
}
