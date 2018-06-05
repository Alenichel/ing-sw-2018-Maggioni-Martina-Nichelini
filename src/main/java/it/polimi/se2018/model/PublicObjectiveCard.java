package it.polimi.se2018.model;

import it.polimi.se2018.strategy.objective.*;
import it.polimi.se2018.utils.ObjectiveCardsName;

public class PublicObjectiveCard extends ObjectiveCard{
    private int points;
    private ObjectiveCardsName name;
    private ScorePointStrategy scorePointStrategy;

    private ScorePointStrategy nameToObjectObjective(ObjectiveCardsName ocn){
        switch (ocn.toString()){
            case "RowColorVariety" :
                this.points = 6;
                this.description = "Rows with no repeated colors ";
                return new RowVariety(VarietyType.COLOR);
            case "ColumnColorVariety" :
                this.points = 5;
                this.description = "Columns with no repeated colors ";
                return new ColumnVariety(VarietyType.COLOR);
            case "RowShadeVariety" :
                this.points = 5;
                this.description = "Rows with no repeated colors ";
                return new RowVariety(VarietyType.SHADE);
            case "ColumnShadeVariety" :
                this.points = 4;
                this.description = "Columns with no repeated value ";
                return new ColumnVariety(VarietyType.COLOR);
            case "LightShades" :
                this.points = 2;
                this.description = "Sets of 1 & 2 values anywhere ";
                return new Shades("light");
            case "MediumShades" :
                this.points = 2;
                this.description = "Sets of 3 & 4 values anywhere ";
                return new Shades("medium");
            case "DarkShades" :
                this.points = 2;
                this.description = "Sets of 5 & 6 values anywhere ";
                return new Shades("dark");
            case "ShadeVariety" :
                this.points = 5;
                this.description = "Set of one of each value anywhere ";
                return new ShadeVariety();
            case "ColorDiagonals" :
                this.description = "Count of diagonally adjacent same color dice ";
                return new ColorDiagonals();
            case "ColorVariety" :
                this.points = 4;
                this.description = "Set of one of each color anywhere ";
                return new ColorVariety();
            default :
                throw new IllegalArgumentException();
        }
    }

    public PublicObjectiveCard(ObjectiveCardsName name){
        this.name = name;
        this.scorePointStrategy = nameToObjectObjective(name);
    }

    public int scorePoint(WindowPatternCard windowPatternCard){
        return scorePointStrategy.scorePoint(windowPatternCard);
    }

    @Override
    public String toString() {
        return this.name.toString() + ": " + this.description + "|| Point: " + this.points;
    }
}
