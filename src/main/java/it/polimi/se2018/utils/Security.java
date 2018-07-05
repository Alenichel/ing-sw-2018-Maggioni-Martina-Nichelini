package it.polimi.se2018.utils;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.io.*;

public class Security {

    public static Player getUser(String name){
        for ( Player p: Server.getInstance().getOnlinePlayers() )
            if (p.getNickname().equals(name)) return p;
        return null;
    }

    /**
     * This method checks if the server contains an instance of a player with the same name of the one
     * provided. If true, it returns that instance, otherwise it creates a new player with that name and
     * returns it.
     * @param name of the player to generate
     * @return the player.
     */
    private static Player generateUser(String name) throws  AuthenticationErrorException {
        if (Security.getUser(name) != null) throw new AuthenticationErrorException("UserAlreadyConnected");

        for ( Player p : Server.getInstance().getOfflinePlayers() ){
            if ( p.getNickname().equals(name)) return p;
        }
        return new Player(name);
    }


    private static Player authenticateFromDb(String name, String password) throws AuthenticationErrorException {
        String DB_PATH = "/sagrada_users_db.txt";
        InputStream dbResource = Security.class.getResourceAsStream(DB_PATH);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbResource));) {
            String nextLine = reader.readLine();
            while (!nextLine.equals("end")) {
                nextLine = reader.readLine();
                String[] userInfo = nextLine.split(":");
                if (userInfo[0].equals(name)) {
                    try {
                        if (userInfo[1].equals(password)) {
                            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "user " + name + " has been authenticated");
                            return generateUser(name);
                        }
                    } catch (AuthenticationErrorException e) {
                        return null;
                    }
                }
            }
            throw new AuthenticationErrorException("UserNotFound");


        } catch (IOException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
            return null;
        }
    }

    /**
     * This method looks for the user configuration file and checks if username and (if required) password are correct.
     * In that case it returns the instance of the authenticated player. Otherwise it returns null.
     * @param name player's username
     * @param password player's password
     * @return instance or null
     */
    public static Player authenticateUser(String name, String password) throws AuthenticationErrorException {

        if (Server.getInstance().isConfigurationRequired()) return authenticateFromDb(name, password);
        else return generateUser(name);
    }
}
