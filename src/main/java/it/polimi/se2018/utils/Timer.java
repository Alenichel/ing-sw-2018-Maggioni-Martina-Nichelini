package it.polimi.se2018.utils;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.controller.RoundHandler;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;

public class Timer extends Thread {

    private long duration;
    private TimerInterface timerInterface;

    public Timer(TimerInterface timerInterface, long duration){
        this.duration = duration;
        this.timerInterface = timerInterface;
    }

    @Override
    public void run() {
        try {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Timer["+ this.getId() + "]started");
            long slept = 0;
            while (slept < duration*1000){
                sleep(1000);
                slept += 1000;
                if (timerInterface instanceof GameController)
                    ((GameController)timerInterface).gameAssociated.setTimerSecondLeft((int)(duration-slept/1000));
                if( timerInterface instanceof RoundHandler){
                    ((RoundHandler)timerInterface).gameAssociated.setTimerSecondLeft((int)(duration-slept/1000));

                }
            }

        }catch (InterruptedException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.WARNING,"Timer[n" + this.getId() + "]interrupted");
            Thread.currentThread().interrupt();
            return;
        }
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION,"Timer[n"+ this.getId() + "]done");
        timerInterface.timerDoneAction();
    }
}