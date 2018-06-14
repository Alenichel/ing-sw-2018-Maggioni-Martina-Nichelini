package it.polimi.se2018.utils;

import it.polimi.se2018.exception.AuthenticationErrorException;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Security {

    /**
     * This method check if the server contains an instance of a player with the same name of the one
     * provided. If true, it returns that instance, otherwise it create a new player whit that name and
     * returns it.
     * @param name of the player to generate
     * @return the player.
     */
    private static Player generateUser(String name) throws  AuthenticationErrorException{
        for ( Player p: Server.getInstance().getOnlinePlayers() )
            if (p.getNickname().equals(name)) {
                throw new AuthenticationErrorException("UserAlreadyConnected");
            }

        for ( Player p : Server.getInstance().getOfflinePlayers() ){
            if ( p.getNickname().equals(name)) return p;
        }
        return new Player(name);
    }

    /**
     * This method look for user configuration file and check if username and password (if required) are right. In that case
     * it returns the instance of the authenticated player. Otherwise it returns null.
     * @param name
     * @param password
     * @return
     * @throws AuthenticationErrorException
     */
    public static Player authenticateUser(String name, String password) throws AuthenticationErrorException {
        String DB_PATH = "/sagrada_users_db.txt";
        InputStream dbResource = Security.class.getResourceAsStream(DB_PATH);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbResource));) {
            String nextLine = reader.readLine();
            while (!nextLine.equals("end")) {
                nextLine = reader.readLine();
                String[] userInfo = nextLine.split(":");
                if (userInfo[0].equals(name)) {
                    try {
                        if (Server.getInstance().isConfigurationRequired()) {
                            if (userInfo[1].equals(password)) {
                                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "user " + name + " has been authenticated");
                                return generateUser(name);
                            }
                        } else {
                            return generateUser(name);
                        }
                    } catch (AuthenticationErrorException e) {
                        return null;
                    }
                }
            }
            throw new AuthenticationErrorException("UserNotFound");
        } catch (IOException e){
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
            return null;
        }
    }
}
