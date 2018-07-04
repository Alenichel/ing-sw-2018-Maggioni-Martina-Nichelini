package it.polimi.se2018;

import it.polimi.se2018.utils.TimerHandler;
import it.polimi.se2018.utils.TimerInterface;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for utils
 */
public class UtilsTest implements TimerInterface{

    @Test
    public void timerHandlerTest(){
        long duration = 10;
        long timerID = TimerHandler.registerTimer(this, duration);

        //start timer test
        TimerHandler.startTimer(timerID);
        Assert.assertTrue(TimerHandler.checkTimer(timerID));
    }

    @Test
    @Override
    public void timerDoneAction(){
        Assert.assertTrue(true);
    }
}
