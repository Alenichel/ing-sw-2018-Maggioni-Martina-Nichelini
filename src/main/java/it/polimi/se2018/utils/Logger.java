package it.polimi.se2018.utils;

public final class Logger {

    private Logger(){}

    public static void ERROR(String toLog){
        System.out.println((char) 27 + "[31m"+ "[*] ERROR: " + toLog + (char) 27 + "[3m" );
    }

    public static void WARNING(String toLog){
        System.out.println((char) 27 + "[33"+ "[*] WARNING: " + toLog + (char) 27 + "[3m" );
    }

    public static void NOTIFICATION(String toLog){
        System.out.println("[*] NOTIFICATION: " + toLog);
    }
}
