package it.polimi.se2018.utils;

import java.io.Serializable;

public class Timer extends Thread implements Serializable{
    private long duration;
    private TimerInterface timerInterface;
    public Timer(TimerInterface timerInterface, long duration){
        this.timerInterface = timerInterface;
        this.duration = duration;
    }

    @Override
    public void run() {
        try {
            System.out.println("vai con il count");
            sleep(this.duration);
        }catch (InterruptedException e){
            e.printStackTrace();
            return;
        }
        timerInterface.timerDoneAction();
    }
}
