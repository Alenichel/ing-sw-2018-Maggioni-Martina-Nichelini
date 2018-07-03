package it.polimi.se2018;

import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.enumeration.DiceColor;
import org.junit.Assert;
import org.junit.Test;

public class PrivateObjectiveCardTest {

    @Test
    public void testGetter(){
        PrivateObjectiveCard poc = new PrivateObjectiveCard(DiceColor.red);

        // test for color getter
        Assert.assertEquals(DiceColor.red, poc.getColor());
        System.out.println(poc);
    }
}
