package it.polimi.se2018.utils;

import java.io.Serializable;
import java.util.ArrayList;


public class TimerHandler implements Serializable {
    private static ArrayList<Timer> timerList = new ArrayList<>();

    /**
     * This method offers an interface to record a new timer.
     * @param timerInterface timer interface
     * @param duration of the timer to set.
     * @return the id fo the recorded timer ready to be started.
     */
    public static long registerTimer(TimerInterface timerInterface, long duration) {
        Timer timer = new Timer(timerInterface, duration);
        timerList.add(timer);
        return timer.getId();
    }

    /**
     * Method to start the timer.
     * @param id the timer to start.
     */
    public static void startTimer(long id){
        for (Timer timer: timerList){
            if (timer.getId() == id) timer.start();
        }
    }

    /**
     * Method to stop the timer.
     * @param id the timer to stop.
     */
    public static void stopTimer(long id){
        for (Timer timer: timerList) {
            if (timer.getId() == id) timer.interrupt();
        }
    }

    /**
     * Method to check timer status.
     * @param id of the timer to check.
     * @return true if timer is alive
     */
    public static boolean checkTimer(long id){
        for (Timer timer: timerList){
            if (timer.getId() == id) return timer.isAlive();
        } return false;
    }
}