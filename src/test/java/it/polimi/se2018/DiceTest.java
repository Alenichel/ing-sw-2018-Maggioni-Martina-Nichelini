package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.DiceLocation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Dice's class
 */
public class DiceTest {

    @Test
    public void testGetter(){
        DiceColor color = DiceColor.green;
        DiceLocation location = DiceLocation.BAG;
        int n = 3;
        Dice d = new Dice(color);

        d.setLocation(location);
        d.setNumber(n);
        System.out.println(d);

        //color getter test
        Assert.assertEquals(color.toString(), d.getColor());

        //location getter test
        Assert.assertEquals(location, d.getLocation());

        //number getter test
        Assert.assertEquals(n, d.getNumber());
    }
}
