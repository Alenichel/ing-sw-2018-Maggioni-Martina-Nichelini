package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.toolcard.EnglomiseBrush;
import it.polimi.se2018.strategy.toolcard.GrozingPliers;
import it.polimi.se2018.strategy.toolcard.Lathekin;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.WindowPatternCardsName;
import static org.junit.Assert.*;
import it.polimi.se2018.strategy.toolcard.Lathekin;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ToolCardTest {

    @Test
    public void testToolGeneral(){
        ToolCard t = new ToolCard(new EnglomiseBrush());

        Game g = new Game();
        Game g1 = new Game();

        t.setDescription("bella");
        Assert.assertEquals("bella", t.getDescription());
        Assert.assertNotEquals("brutta", t.getDescription());

        t.setDiceColor("yellow");
        Assert.assertEquals("yellow", t.getDiceColor());
        Assert.assertNotEquals("blue", t.getDiceColor());

        t.setGameReference(g);
        Assert.assertEquals(g, t.getGameReference());
        Assert.assertNotEquals(g1, t.getGameReference());

        t.setUsed(true);
        Assert.assertEquals(true, t.getUsed());
        Assert.assertNotEquals(false, t.getUsed());
        Assert.assertTrue(t.isUsed());
    }

    public void testLathekin(){
        WindowPatternCard windowPatternCard = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        WindowCell wstart1 = windowPatternCard.getGrid()[1][1];
        WindowCell wstart2 = windowPatternCard.getGrid()[2][1];
        WindowCell wend1 = windowPatternCard.getGrid()[1][3];
        WindowCell wend2 = windowPatternCard.getGrid()[2][3];

        Dice diceStart1 = new Dice(DiceColor.Yellow.toString());
        Dice diceStart2 = new Dice(DiceColor.Purple.toString());
        diceStart1.rollDice();
        diceStart2.rollDice();

        try {
            wstart1.setAssignedDice(diceStart1);
            wstart2.setAssignedDice(diceStart2);
        }catch (NotEmptyWindowCellException e){
            fail();
        }
        Lathekin lathekin = new Lathekin();
        ToolCard toolCardProvaLathekin = new ToolCard(lathekin);
        System.out.println(windowPatternCard.toString());
        lathekin = lathekin.Lathekin(lathekin, windowPatternCard, wstart1, wstart2, wend1, wend2);
        try {
            lathekin.executeEffect();
        }catch (ToolCardException | NotEmptyWindowCellException e){
            System.out.print(e);
            fail();
        }

        Assert.assertEquals(windowPatternCard.getGrid()[1][3], wstart1);
        // Assert.assertEquals(windowPatternCard.getGrid()[2][3], wstart2);
        // Assert.assertEquals(windowPatternCard.getGrid()[1][1], null);
        // Assert.assertEquals(windowPatternCard.getGrid()[2][1], null);


    }

}
