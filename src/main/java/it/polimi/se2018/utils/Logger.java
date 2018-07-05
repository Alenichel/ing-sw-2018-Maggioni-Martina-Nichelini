package it.polimi.se2018.utils;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

public final class Logger {

    private Logger(){
        throw new IllegalStateException("Utility class");
    }

    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CLIENT_LOG = "/sagrada_client.log";
    private static final String SERVER_LOG = "/sagrada_server.log";

    private static LoggerType side;
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;
    private static boolean debugMode;

    public static void setSide(LoggerType side, boolean debugMode){
        Logger.side = side;
        Logger.debugMode = debugMode;

        try {
            if (Logger.side == LoggerType.CLIENT_SIDE) fileWriter = new FileWriter(HOME_PATH + CLIENT_LOG, true);
            else fileWriter = new FileWriter(HOME_PATH + SERVER_LOG, true);
        } catch (IOException e) {;}
        printWriter = new PrintWriter(fileWriter, true);
        printWriter.println("\n\n --------- SESSION STARTED --------- " + LocalDate.now() + " / " + LocalTime.now());
    }

    public static void log (LoggerType side, LoggerPriority priority, String toLog){
        if (Logger.side == side) {
            if (priority == LoggerPriority.NORMAL)
                System.out.println(toLog);

            else if (priority == LoggerPriority.NOTIFICATION ){
                toLog = "[*] NOTIFICATION: " + toLog;
                System.out.println(toLog);
                printWriter.println(toLog);
            }
            else if (priority == LoggerPriority.WARNING){
                toLog = "[*] WARNING: " + toLog;
                System.out.println((char) 27 + "[33m" + toLog + (char) 27 + "[3m");
                printWriter.println(toLog);
            }
            else if (priority == LoggerPriority.ERROR){
                toLog = "[*] ERROR: " + toLog;
                System.out.println((char) 27 + "[31m"+ toLog + (char) 27 + "[3m");
                printWriter.println(toLog);
            }
            else if(priority == LoggerPriority.DEBUG && debugMode){
                toLog = "[*] DEBUG: " + toLog;
                System.out.println(toLog);
                printWriter.println(toLog);
            }
        }
    }

}
