package it.polimi.se2018;

import it.polimi.se2018.model.Server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Server server = Server.getInstance();
        System.out.println(server.getDefaultMatchmakingTimer());
        System.out.println(server.getDefaultMoveTimer());
    }
}
