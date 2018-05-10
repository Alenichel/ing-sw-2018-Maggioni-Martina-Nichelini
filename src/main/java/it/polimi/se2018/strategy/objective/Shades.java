package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.ScorePointStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;
import org.omg.IOP.CodecPackage.InvalidTypeForEncoding;

import static java.lang.Integer.min;

public class Shades implements ScorePointStrategy {

    int firstNumber;
    int secondNumber;

    public Shades(String type, int firstNumber, int secondNumber) {
        if (type == "light") {
            this.firstNumber = 1;
            this.secondNumber = 2;
        }
        else if (type == "medium") {
            this.firstNumber = 3;
            this.secondNumber = 4;
        }
        else if (type == "dark") {
            this.firstNumber = 5;
            this.secondNumber = 6;
        }
        //else {throw  new InvalidTypeForEncoding()}

    }

    private boolean isFirst(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== firstNumber) return true;
            else return false;
        }
    }

    private boolean isSecond(WindowCell a) {
        if(a==null) return false;
        if(a.getAssignedDice()==null) return false;
        else {
            if (a.getAssignedDice().getNumber()== secondNumber) return true;
            else return false;
        }
    }


    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int first = 0;
        int second = 0;
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isFirst(grid[i][j])) {
                    first += 1;
                }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (isSecond(grid[i][j])) {
                    second +=1;
                }
        scoreCounter = 2*min(first, second);
        return scoreCounter;
    }
}
