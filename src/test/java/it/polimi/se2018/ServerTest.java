package it.polimi.se2018;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;


public class ServerTest {

    @Test
    public void testGetter() {
        Server server = Server.getInstance();

        Player p = new Player("Player");


        server.getCurrentGame();
        server.getActiveGames();
        server.getDefaultMatchmakingTimer();
        server.getWaitingPlayers();
        server.addPlayerToOnlinePlayers(p);
        server.isConfigurationRequired();
        server.getOfflinePlayers();

        Assert.assertEquals(1, server.getOnlinePlayers().size());

        server.removePlayerFromOnlinePlayers(p);
        Assert.assertTrue(!server.getOnlinePlayers().contains(p)); //check the player is not anymore listed in online players.
        Assert.assertTrue(server.getOfflinePlayers().contains(p)); //check the player is now listed in offline players

    }

}