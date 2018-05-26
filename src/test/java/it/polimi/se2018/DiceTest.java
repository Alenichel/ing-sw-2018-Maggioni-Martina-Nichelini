package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.utils.DiceLocation;
import org.junit.Assert;
import org.junit.Test;

public class DiceTest {

    @Test
    public void testGetter(){
        String color = "green";
        DiceLocation location = DiceLocation.BAG;
        int n = 3;
        Dice d = new Dice(color);

        d.setLocation(location);
        d.setNumber(n);

        //Assert.assertEquals(COLOR, d.getColor());
        Assert.assertEquals(location, d.getLocation());
        Assert.assertEquals(n, d.getNumber());
    }
}
