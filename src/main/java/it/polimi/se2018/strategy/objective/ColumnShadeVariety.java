package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class ColumnShadeVariety implements ScorePointStrategy {

    private WindowPatternCard windowPatternCard;
    private WindowCell a;
    private int i;
    private int j;

    public ColumnShadeVariety(WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    public ColumnShadeVariety(ColumnShadeVariety columnShadeVariety, WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        for (int j = 0; j < 5; j++)
            if (a[i][j].getNumber() != a[i + 1][j].getNumber() && a[i][j].getNumber() != a[i + 2][j].getNumber() && a[i][j].getNumber() != a[i+3][j].getNumber()) ;
        //player.score +=4;
        return 0;
    }
}

