package it.polimi.se2018.utils;

import javax.management.timer.TimerNotification;
import java.io.Serializable;
import java.util.ArrayList;


public class TimerHandler implements Serializable {
    private static ArrayList<Timer> timerList = new ArrayList<>();

    public static long registerTimer(TimerInterface timerInterface, long duration) {
        Timer timer = new Timer(timerInterface, duration);
        timerList.add(timer);
        return timer.getId();
    }

    public static void startTimer(long id){
        for (Timer timer: timerList){
            if (timer.getId() == id) timer.start();
        }
    }

    public static void stopTimer(long id){
        for (Timer timer: timerList) {
            if (timer.getId() == id) timer.interrupt();
        }
    }

    public static boolean checkTimer(long id){
        for (Timer timer: timerList){
            if (timer.getId() == id) return timer.isAlive();
        } return false;
    }
}
