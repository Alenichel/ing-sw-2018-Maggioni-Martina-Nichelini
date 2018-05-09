package it.polimi.se2018;

import it.polimi.se2018.Exception.ForbiddenDiceInsert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;

public class WindowPatternCardTest {

    private WindowPatternCard wpc = new WindowPatternCard("viaLux");
    private WindowCell[][] exampleGrid = new WindowCell[4][5];

    @Before
    public void initialize(){
        for (int i = 0; i < 4; i ++){
            for (int j = 0; j < 5; j++){
                exampleGrid[i][j] = new WindowCell();
            }
        }

        exampleGrid[0][1].setAssignedDice(new Dice("red", null));
    }

    @Test
    public void insertDiceTest(){
        try {
            wpc.insertDice(new Dice("red", null), wpc.getCell(0, 1), false);
        } catch (ForbiddenDiceInsert | NullPointerException e) {
            System.out.println(e);
            fail();
        }

        for (int i = 0; i < 4; i++){
            assertArrayEquals(exampleGrid[i], wpc.getGrid()[i]);
        }

    }
}
