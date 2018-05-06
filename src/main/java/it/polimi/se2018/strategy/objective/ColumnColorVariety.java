package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class ColumnColorVariety implements ScorePointStrategy {

    private WindowPatternCard windowPatternCard;
    private WindowCell a;
    private int i;
    private int j;

    public ColumnColorVariety(WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    public ColumnColorVariety(ColumnColorVariety columnColorVariety, WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        for (int j = 0; j < 5; j++)
            if (a[i][j].getColor() != a[i + 1][j].getColor() && a[i][j].getColor() != a[i + 2][j].getColor() && a[i][j].getColor() != a[i+3][j].getColor()) ;
                //player.score +=5;
                return 0;
            }
    }
}