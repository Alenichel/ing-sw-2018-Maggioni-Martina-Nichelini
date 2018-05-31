package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.toolcard.*;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.WindowPatternCardsName;
import static org.junit.Assert.*;
import it.polimi.se2018.strategy.toolcard.Lathekin;
import org.junit.Assert;
import org.junit.Test;

import java.security.spec.ECField;
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

        t.setDiceColor(DiceColor.yellow);
        Assert.assertEquals(DiceColor.yellow, t.getDiceColor());
        Assert.assertNotEquals(DiceColor.blue, t.getDiceColor());

        t.setGameReference(g);
        Assert.assertEquals(g, t.getGameReference());
        Assert.assertNotEquals(g1, t.getGameReference());

        t.setUsed(true);
        Assert.assertEquals(true, t.getUsed());
        Assert.assertNotEquals(false, t.getUsed());
        Assert.assertTrue(t.isUsed());
    }

    @Test
    public void testLathekin(){
        WindowPatternCard windowPatternCard = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        WindowCell wstart1 = windowPatternCard.getGrid()[1][1]; //2 2
        WindowCell wstart2 = windowPatternCard.getGrid()[2][1]; //3 2
        WindowCell wend1 = windowPatternCard.getGrid()[1][3];   //2 4
        WindowCell wend2 = windowPatternCard.getGrid()[2][3];   //3 4

        Dice diceStart1 = new Dice(DiceColor.green);
        Dice diceStart2 = new Dice(DiceColor.purple);
        //System.out.println(diceStart1.toString());
        //System.out.println(diceStart2.toString());
        try {
            wstart1.setAssignedDice(diceStart1);
            wstart2.setAssignedDice(diceStart2);
        }catch (NotEmptyWindowCellException e){
            System.out.println(".....");
            fail();
        }
        Lathekin lathekin = new Lathekin();
        ToolCard toolCardProvaLathekin = new ToolCard(lathekin);
        //System.out.println(windowPatternCard.toString());
        lathekin = lathekin.Lathekin(lathekin, windowPatternCard, wstart1, wstart2, wend1, wend2);
        try {
            lathekin.executeEffect();
        }catch (ToolCardException | NotEmptyWindowCellException e){
            System.out.print(e);
            fail();
        }

        Assert.assertEquals(windowPatternCard.getGrid()[1][3].getAssignedDice(), diceStart1);
        Assert.assertEquals(windowPatternCard.getGrid()[2][3].getAssignedDice(), diceStart2);
        Assert.assertEquals(windowPatternCard.getGrid()[1][1].getAssignedDice(), null);
        Assert.assertEquals(windowPatternCard.getGrid()[2][1].getAssignedDice(), null);
    }

    @Test
    public void testEnglomiseBrush() {
        Dice d = new Dice(DiceColor.purple);
        EnglomiseBrush englomiseBrush = new EnglomiseBrush();
        WindowPatternCard w = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        englomiseBrush = englomiseBrush.EnglomiseBrush(englomiseBrush, w, w.getCell(1, 1), w.getCell(0, 1));
        //0,1-->1,2
        //System.out.println(w.toString());

        try {
            w.insertDice(d, 1, 1, true, true, false);
        } catch (NotValidInsertion | NotEmptyWindowCellException e) {
            System.out.println(e+"1--");
            fail();
        }
        //System.out.println(w.toString());


        try {
            englomiseBrush.executeEffect();
        } catch (ToolCardException e) {
            System.out.println(e+"2--");
            fail();
        } catch (NotEmptyWindowCellException e) {
            System.out.println(e);
            fail();
        }

        System.out.println(w.toString());

        Assert.assertEquals(w.getGrid()[0][1].getAssignedDice(), d);
        Assert.assertEquals(w.getGrid()[1][1].getAssignedDice(), null);
    }

    @Test
    public void testGrozingPliers(){
        Dice d = new Dice(DiceColor.red);
        GrozingPliers grozingPliers = new GrozingPliers();
        grozingPliers.GrozingPliers(grozingPliers,d, true);
        int n = d.getNumber();
        grozingPliers.executeEffect();

        if (n != 6)
            Assert.assertEquals(n+1, d.getNumber());
        else
            Assert.assertEquals(n, d.getNumber());

    }

    @Test
    public void testCopperFoilBurnisher(){
        WindowPatternCard w = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        WindowCell s = w.getCell(1,1);
        WindowCell e = w.getCell(0,4);
        Dice d = new Dice(DiceColor.red);
        CopperFoilBurnisher copperFoilBurnisher = new CopperFoilBurnisher();
        copperFoilBurnisher.CopperFoilBurnisher(copperFoilBurnisher, w, s, e);
        //System.out.println(w.toString());

        try {
            w.insertDice(d, s.getRow(), s.getColumn(), true, true, false);
        }catch (Exception p){
            System.out.println(p+"1---");
        }
        try {
            copperFoilBurnisher.executeEffect();
        }catch (Exception p){
            System.out.println(p+"2 ---");
        }

        //System.out.println(w.toString());


    }
}
