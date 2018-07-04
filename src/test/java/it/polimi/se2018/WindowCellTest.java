package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.enumeration.DiceColor;
import org.junit.Assert;
import org.junit.Test;

public class WindowCellTest {

    @Test
    public void testGetter() throws NotEmptyWindowCellException{
        int row = 1;
        int wrongRow = 2;
        int column = 1;
        int wrongColumn = 4;
        DiceColor color = DiceColor.green;
        Dice d = new Dice(color);
        Dice wrongD = new Dice(color);
        WindowCell wc = new WindowCell(row, column);

        //row getter test
        Assert.assertEquals(row, wc.getRow());
        Assert.assertNotEquals(wrongRow, wc.getRow());

        //column getter test
        Assert.assertEquals(column, wc.getColumn());
        Assert.assertNotEquals(wrongColumn, wc.getColumn());

        //assigned dice getter test
        wc.setAssignedDice(d);
        Assert.assertEquals(d, wc.getAssignedDice());
        Assert.assertNotEquals(wrongD, wc.getAssignedDice());

        //color constraint getter test
        String cc = "green";
        WindowCell wc2 = new WindowCell(row, column, cc);
        Assert.assertEquals(cc, wc2.getColorConstraint());
        Assert.assertNotEquals(null, wc2.getColorConstraint());

        //number constraint getter test
        int nc = 5;
        WindowCell wc3 = new WindowCell(row, column, nc);
        Assert.assertEquals(nc, wc3.getNumberConstraint());
        Assert.assertNotEquals(null, wc3.getNumberConstraint());
    }

    public void testEquals(){
        WindowCell wc3 = new WindowCell(1, 4, "green");
        WindowCell wc4 = new WindowCell(1, 4, "green");
        Assert.assertTrue(wc3.equals(wc4));
    }
}
