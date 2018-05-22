package it.polimi.se2018.utils;

import javax.management.timer.TimerNotification;
import java.io.Serializable;
import java.util.ArrayList;


public class TimerHandler implements Serializable {
    public static TimerHandler instance;
    ArrayList<Timer> timerList = new ArrayList<>();

    public static TimerHandler getInstance() {
        if(instance == null){
            instance = new TimerHandler();
        }
        return instance;
    }

    public long registerTimer(TimerInterface timerInterface, long duration) {
        Timer timer = new Timer(timerInterface, duration);
        timerList.add(timer);
        return timer.getId();
    }

    public void startTimer(long id){
        for (Timer timer: this.timerList){
            if (timer.getId() == id) timer.start();
        }
    }
}
