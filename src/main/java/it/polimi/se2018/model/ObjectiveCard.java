package it.polimi.se2018.model;

public class ObjectiveCard extends Card {
    protected String description;
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

    @Override
    public String toString() {
        return this.getClass().toString();
    }
}
