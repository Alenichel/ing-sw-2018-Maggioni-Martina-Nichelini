package it.polimi.se2018.model;

import it.polimi.se2018.strategy.objective.*;
import it.polimi.se2018.utils.ObjectiveCardsName;

public class PublicObjectiveCard extends ObjectiveCard{
    private ScorePointStrategy scorePointStrategy;

    private ScorePointStrategy nameToObjectObjective(ObjectiveCardsName ocn){
        switch (ocn.toString()){
            case "RowColorVariety" : return new RowVariety(VarietyType.COLOR);
            case "ColumnColorVariety" : return new ColumnVariety(VarietyType.COLOR);
            case "RowShadeVariety" : return new RowVariety(VarietyType.SHADE);
            case "ColumnShadeVariety" : return new ColumnVariety(VarietyType.COLOR);
            case "LightShades" : return new Shades("light");
            case "MediumShades" : return new Shades("medium");
            case "DarkShades" : return new Shades("dark");
            case "ShadeVariety" : return new ShadeVariety();
            case "ColorDiagonals" : return new ColorDiagonals();
            case "ColorVariety" : return new ColorVariety();
            default :
                throw new IllegalArgumentException();
        }
    }

    public PublicObjectiveCard(ObjectiveCardsName name){
        this.scorePointStrategy = nameToObjectObjective(name);
    }

    public int scorePoint(WindowPatternCard windowPatternCard){
        return scorePointStrategy.scorePoint(windowPatternCard);
    }
}
