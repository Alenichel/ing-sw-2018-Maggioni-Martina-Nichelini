package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.strategy.objective.ColumnVariety;
import org.junit.Assert;
import org.junit.Test;

public class ObjectivesTest {

    @Test
    public void testColumnVariety() throws NotValidInsertion, NotEmptyWindowCellException{
        WindowPatternCard wpc = new WindowPatternCard("auroraeMagnificus");

        Dice d1 = new Dice("purple");
        Dice d2 = new Dice("yellow");
        Dice d3 = new Dice("red");
        Dice d4 = new Dice("blue");

        wpc.insertDice(d1, 0, 0, false,false);
        wpc.insertDice(d2, 1,0,false,false);
        wpc.insertDice(d3, 2,0,false,false);
        wpc.insertDice(d4, 3,0,false,false);

        wpc.insertDice(d1, 0, 2, false,false);
        wpc.insertDice(d2, 1,2,false,false);
        wpc.insertDice(d3, 2,2,false,false);
        wpc.insertDice(d4, 3,2,false,false);

        wpc.insertDice(d1, 0, 4, false,false);
        wpc.insertDice(d1, 1,4,false,false);
        wpc.insertDice(d3, 2,4,false,false);
        wpc.insertDice(d4, 3,4,false,false);

        ScorePointStrategy sps = new ColumnVariety("Color");
        Assert.assertEquals(10, sps.scorePoint(wpc));
    }

    @Test
    public void testEmptyGrid(){
        WindowPatternCard wpc = new WindowPatternCard("auroraeMagnificus");
        ScorePointStrategy sps = new ColumnVariety("Color");
        Assert.assertEquals(0, sps.scorePoint(wpc));
    }
}
