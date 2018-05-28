package it.polimi.se2018.utils;

import it.polimi.se2018.controller.GameController;

public class Timer extends Thread{

    private long duration;
    private TimerInterface timerInterface;

    public Timer(TimerInterface timerInterface, long duration){
        this.duration = duration;
        this.timerInterface = timerInterface;
    }


    @Override
    public void run() {
        try {
            Logger.NOTIFICATION(LoggerType.SERVER_SIDE,"Timer started");
            long slept = 0;
            while (slept < duration*1000){
                sleep(1000);
                slept += 1000;
                if (timerInterface instanceof GameController)
                    ((GameController)timerInterface).gameAssociated.setTimerSecondLeft((int)(duration-slept/1000));
            }

        }catch (InterruptedException e){
            Logger.WARNING(LoggerType.SERVER_SIDE, "Timer interrupted");
            Thread.currentThread().interrupt();
            return;
        }
        Logger.NOTIFICATION(LoggerType.SERVER_SIDE,"Timer done");
        timerInterface.timerDoneAction();
    }
}
