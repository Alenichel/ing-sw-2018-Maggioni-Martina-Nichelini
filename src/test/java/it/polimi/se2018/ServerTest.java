package it.polimi.se2018;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Tests for Server's class
 */
public class ServerTest {

    @Test
    public void testGetter() {
        Server server = Server.getInstance();

        Player p = new Player("Player");

        //add player from online players test
        server.addPlayerToOnlinePlayers(p);
        Assert.assertEquals(1, server.getOnlinePlayers().size());
        Assert.assertNotEquals(3, server.getOnlinePlayers().size());

        //remove player from online players test
        server.removePlayerFromOnlinePlayers(p);
        Assert.assertTrue(!server.getOnlinePlayers().contains(p));
        Assert.assertTrue(server.getOfflinePlayers().contains(p));

    }

}