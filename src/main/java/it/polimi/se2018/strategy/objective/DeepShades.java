package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowPatternCard;

public class DeepShades implements ScorePointStrategy {
    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        return 1;
    }
}
