package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class RowShadeVariety implements ScorePointStrategy{

    private WindowPatternCard windowPatternCard;
    private WindowCell a;
    private int i;
    private int j;

    public RowShadeVariety(WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    public RowShadeVariety(RowShadeVariety rowShadeVariety, WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        for (i = 0; i < 4; i++)
            if (a[i][j].getNumber() != a[i][j + 1].getNumber() && a[i][j].getNumber() != a[i][j + 2].getNumber() && a[i][j].getNumber() != a[i][j + 3].getNumber() && a[i][j].getNumber() != a[i][j + 4].getNumber());
        //player.score +=6;

        return 0;
    }
}
