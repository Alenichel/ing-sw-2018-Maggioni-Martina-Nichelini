package it.polimi.se2018;

import org.junit.Assert;
import org.junit.Test;

public class WindowCellTest {

    @Test
    public void testGetter(){
        int nc = 5;
        int row = 1;
        int column = 1;
        String color = "green";
        Dice d = new Dice(color);
        WindowCell wc = new WindowCell(row, column, nc);
        wc.setAssignedDice(d);
        Assert.assertEquals(wc.getNumberConstraint(), wc.getNumberConstraint());
        Assert.assertEquals(row, wc.getRow());
        Assert.assertEquals(column, wc.getColumn());
        Assert.assertEquals(d, wc.getAssignedDice());

        String cc = "green";
        WindowCell wc2 = new WindowCell(row, column, cc);
        Assert.assertEquals(cc, wc2.getColorConstraint());
    }

    public void testEquals(){
        WindowCell wc3 = new WindowCell(1, 4, "green");
        WindowCell wc4 = new WindowCell(1, 4, "green");
        Assert.assertTrue(wc3.equals(wc4));
    }
}
