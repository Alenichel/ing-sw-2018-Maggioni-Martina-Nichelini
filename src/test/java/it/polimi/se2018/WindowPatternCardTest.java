package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class WindowPatternCardTest {

    @Test
    public void testValidInsertion() throws NotValidInsertion, NotEmptyWindowCellException{
        Dice d = new Dice("green");
        WindowPatternCard wpc = new WindowPatternCard("auroraeMagnificus");

        wpc.insertDice(d,0,0,true,true);
        Assert.assertEquals(d, wpc.getCell(0,0).getAssignedDice());
    }

    @Test
    public void testConstraintViolation() throws NotEmptyWindowCellException{
        Dice d = new Dice("green");
        WindowPatternCard wpc = new WindowPatternCard("auroraeMagnificus");

        try {
            wpc.insertDice(d,0,1,true,true);
        } catch (NotValidInsertion e){
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testPositionViolation() throws NotEmptyWindowCellException{
        Dice d = new Dice("green");
        WindowPatternCard wpc = new WindowPatternCard("auroraeMagnificus");

        try {
            wpc.insertDice(d,0,0,true,true);
            wpc.insertDice(d, 1,0,true,true);
        } catch (NotValidInsertion e){
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

}
