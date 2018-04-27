package it.polimi.se2018;

public class PublicObjectiveCard extends ObjectiveCard{
    private ScorePointStrategy scorePointStrategy;

    public PublicObjectiveCard(ScorePointStrategy scorePointStrategy){
        this.scorePointStrategy = scorePointStrategy;
    }

    public int scorePoint(){
        return scorePointStrategy.scoreEffect();
    }
}
