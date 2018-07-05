package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.WindowPatternCardsName;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for window pattern cards
 */
public class WindowPatternCardTest {

    @Test
    //valid insertion test
    public void testValidInsertion() throws NotValidInsertion, NotEmptyWindowCellException{
        Die d = new Die(DiceColor.green);
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        System.out.println(wpc);

        wpc.insertDice(d,0,1,true, true,false);
        Assert.assertEquals(d, wpc.getCell(0,1).getAssignedDie());
    }

    @Test
    //constraint violation test
    public void testConstraintViolation() throws NotEmptyWindowCellException{
        Die d = new Die(DiceColor.green);
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        try {
            wpc.insertDice(d,0,1,true, true,true);
        } catch (NotValidInsertion e){
            Assert.assertTrue(true);
            return;
        }
        //Assert.assertTrue(false);
    }

    @Test
    //position violation test
    public void testPositionViolation() throws NotEmptyWindowCellException{
        Die d = new Die(DiceColor.green);
        WindowPatternCard wpc = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);

        try {
            wpc.insertDice(d,0,0,true, true,true);
            wpc.insertDice(d, 1,0,true, true,true);
        } catch (NotValidInsertion e){
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

}
