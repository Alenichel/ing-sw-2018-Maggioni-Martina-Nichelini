package it.polimi.se2018.utils;

import static java.lang.Thread.sleep;

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
            Logger.NOTIFICATION("Timer started");
            sleep(this.duration*1000);
        }catch (InterruptedException e){
            e.printStackTrace();
            return;
        }
        timerInterface.timerDoneAction();
    }
}
