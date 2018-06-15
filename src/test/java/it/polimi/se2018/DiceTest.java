package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.DiceLocation;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(color.toString(), d.getColor());
        Assert.assertEquals(location, d.getLocation());
        Assert.assertEquals(n, d.getNumber());
    }
}
