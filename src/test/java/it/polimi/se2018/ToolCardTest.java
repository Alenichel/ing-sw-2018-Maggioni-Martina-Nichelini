package it.polimi.se2018;

import it.polimi.se2018.model.*;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.ToolCardsName;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for Tool card class
 */
public class ToolCardTest {

    @Test
    public void testToolCard() {
        ToolCard t = new ToolCard(ToolCardsName.EnglomiseBrush);
        System.out.println(t);

        Game g = new Game();
        Game g1 = new Game();

        //description getter test
        t.setDescription("bella");
        Assert.assertEquals("bella", t.getDescription());
        Assert.assertNotEquals("brutta", t.getDescription());

        //dice color getter test
        t.setDiceColor(DiceColor.yellow);
        Assert.assertEquals(DiceColor.yellow, t.getDiceColor());
        Assert.assertNotEquals(DiceColor.blue, t.getDiceColor());

        //game reference getter test
        t.setGameReference(g);
        Assert.assertEquals(g, t.getGameReference());
        Assert.assertNotEquals(g1, t.getGameReference());

        //is used method test
        t.setUsed(true);
        Assert.assertEquals(true, t.isUsed());
        Assert.assertNotEquals(false, t.isUsed());
        Assert.assertTrue(t.isUsed());
    }
}


