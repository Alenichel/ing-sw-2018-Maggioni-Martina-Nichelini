package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;

public class RowColorVariaty implements ScorePointStrategy {
    @Override
    public int scorePoint() {
        return 2;
    }
}