package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class RowColorVariety implements ScorePointStrategy {

    private WindowPatternCard windowPatternCard;
    private WindowCell a;
    private int i;
    private int j;

    public RowColorVariety(WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    public RowColorVariety(RowColorVariety rowColorVariety, WindowPatternCard windowPatternCard, WindowCell a, int i, int j) {
        this.windowPatternCard = windowPatternCard;
        this.a = a;
        this.i = i;
        this.j = j;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        for (i=0; i<4; i++)
            if (a[i][j].getColor() != a[i][j + 1].getColor() && a[i][j].getColor() != a[i][j + 2].getColor() && a[i][j].getColor() != a[i][j + 3].getColor() && a[i][j].getColor() != a[i][j + 4].getColor());
        //player.score +=6;

        return 0;
    }

}