package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.PrivateShadesOfColor;
import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.strategy.objective.*;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.WindowPatternCardsName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectivesTest {

    @Test
    public void testColumnVariety() throws NotValidInsertion, NotEmptyWindowCellException{
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("purple");
        Dice d2 = new Dice("yellow");
        Dice d3 = new Dice("red");
        Dice d4 = new Dice("blue");

        wpc.insertDice(d1, 0, 0, false,false,false);
        wpc.insertDice(d2, 1,0,false,false,false);
        wpc.insertDice(d3, 2,0,false, false,false);
        wpc.insertDice(d4, 3,0,false,false, false);

        wpc.insertDice(d1, 0, 2, false,false,false);
        wpc.insertDice(d2, 1,2,false, false, false);
        wpc.insertDice(d3, 2,2,false, false, false);
        wpc.insertDice(d4, 3,2,false, false, false);

        wpc.insertDice(d1, 0, 4, false, false, false);
        wpc.insertDice(d1, 1,4,false,false,false);
        wpc.insertDice(d3, 2,4,false,false, false);
        wpc.insertDice(d4, 3,4,false, false,false);

        ScorePointStrategy sps = new ColumnVariety(VarietyType.COLOR);
        Assert.assertEquals(10, sps.scorePoint(wpc));
    }

    @Test
    public void testRowVariety() throws NotValidInsertion, NotEmptyWindowCellException{
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("purple");
        Dice d2 = new Dice("yellow");
        Dice d3 = new Dice("red");
        Dice d4 = new Dice("blue");
        Dice d5 = new Dice("green");

        wpc.insertDice(d1, 0, 0, false,false, false);
        wpc.insertDice(d2, 0,1,false,false, false);
        wpc.insertDice(d3, 0,2,false,false, false);
        wpc.insertDice(d4, 0,3,false,false, false);
        wpc.insertDice(d5, 0,4,false, false, false);

        wpc.insertDice(d1, 1, 0, false,false, false);
        wpc.insertDice(d2, 1,1,false,false,false);
        wpc.insertDice(d3, 1,2,false,false, false);
        wpc.insertDice(d4, 1,3,false, false, false);
        wpc.insertDice(d5, 1,4,false,false,false);

        wpc.insertDice(d1, 2, 0, false,false, false);
        wpc.insertDice(d1, 2,1,false,false,false);
        wpc.insertDice(d3, 2,2,false,false,false);
        wpc.insertDice(d4, 2,3,false,false,false);
        wpc.insertDice(d5, 2,4,false,false,false);

        ScorePointStrategy sps = new RowVariety(VarietyType.COLOR);
        Assert.assertEquals(12, sps.scorePoint(wpc));

    }

    @Test
    public void testColorVariety() throws  NotValidInsertion, NotEmptyWindowCellException{
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("purple");
        Dice d2 = new Dice("yellow");
        Dice d3 = new Dice("red");
        Dice d4 = new Dice("blue");
        Dice d5 = new Dice("green");

        wpc.insertDice(d1, 0, 0, false,false,false);
        wpc.insertDice(d2, 1,1,false,false,false);
        wpc.insertDice(d3, 2,2,false,false,false);
        wpc.insertDice(d4, 3,3,false,false,false);
        wpc.insertDice(d5, 3,2,false,false,false);

        wpc.insertDice(d1, 3,4, false,false,false);
        wpc.insertDice(d1, 2,1,false,false,false);

        ScorePointStrategy sps = new ColorVariety();
        Assert.assertEquals(4,sps.scorePoint(wpc));

    }

    @Test
    public void testShadeVariety() throws NotValidInsertion, NotEmptyWindowCellException{
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("purple", 1);
        Dice d2 = new Dice("yellow",2);
        Dice d3 = new Dice("red",3);
        Dice d4 = new Dice("blue",4);
        Dice d5 = new Dice("green",5);
        Dice d6 = new Dice("green", 6);

        wpc.insertDice(d1, 0, 0, false,false,false);
        wpc.insertDice(d2, 1,1,false,false,false);
        wpc.insertDice(d3, 2,2,false,false,false);
        wpc.insertDice(d4, 3,3,false,false,false);
        wpc.insertDice(d5, 3,2,false,false,false);
        wpc.insertDice(d6, 0,1,false,false,false);

        wpc.insertDice(d1, 3,4, false,false,false);
        wpc.insertDice(d1, 2,1,false,false,false);
        ScorePointStrategy sps = new ShadeVariety();
        Assert.assertEquals(5,sps.scorePoint(wpc));
    }

    @Test
    public void testShades() throws NotValidInsertion, NotEmptyWindowCellException {
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("red",3);
        Dice d2 = new Dice("blue",4);
        Dice d3 = new Dice("purple",3);
        Dice d4 = new Dice("red",4);
        Dice d5 = new Dice("yellow", 5);

        wpc.insertDice(d1, 0, 0, false,false,false);
        wpc.insertDice(d2, 1,1,false,false,false);
        wpc.insertDice(d3, 1,2,false,false,false);
        wpc.insertDice(d4, 3,2,false,false,false);
        wpc.insertDice(d5, 1,3,false,false,false);

        ScorePointStrategy sps = new Shades("medium");
        Assert.assertEquals(4,sps.scorePoint(wpc));
    }

   /* @Test
    public void testColorDiagonals() throws NotValidInsertion, NotEmptyWindowCellException {
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("yellow");
        Dice d2 = new Dice("blue");

        wpc.insertDice(d2, 0, 0, false,false);
        wpc.insertDice(d2, 0, 2, false,false);
        wpc.insertDice(d2, 1,1,false,false);
        wpc.insertDice(d2, 2,2,false,false);
        wpc.insertDice(d2, 3,3,false,false);
        wpc.insertDice(d1, 0,3,false,false,false);
        wpc.insertDice(d1, 2,3,false,false,false);
        wpc.insertDice(d1, 1,4,false,false,false);
        wpc.insertDice(d1, 3,2,false,false,false);
        wpc.insertDice(d1, 1,2,false,false,false);

        System.out.println(wpc);
        ScorePointStrategy sps = new ColorDiagonals();
        Assert.assertEquals(7,sps.scorePoint(wpc));
    }*/

    @Test
    public void testPrivateShadesOfColor() throws NotValidInsertion, NotEmptyWindowCellException {
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        Dice d1 = new Dice("yellow");
        Dice d2 = new Dice("red");
        Dice d3 = new Dice("blue");
        Dice d4 = new Dice("blue");
        Dice d5 = new Dice("blue");

        wpc.insertDice(d1, 0, 0, false,false,false);
        wpc.insertDice(d2, 1,2,false,false,false);
        wpc.insertDice(d3, 3,2,false,false,false);
        wpc.insertDice(d4, 2,3,false,false,false);
        wpc.insertDice(d5, 3,4,false,false,false);

        PrivateShadesOfColor sp = new PrivateShadesOfColor(DiceColor.blue);
        Assert.assertEquals(3, sp.scorePoint(wpc));
    }

    @Test
    public void testEmptyGrid(){
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        ScorePointStrategy sps = new ColumnVariety(VarietyType.COLOR);
        ScorePointStrategy sps2 = new ColorVariety();
        Assert.assertEquals(0, sps.scorePoint(wpc));
        Assert.assertEquals(0, sps2.scorePoint(wpc));
    }
}
