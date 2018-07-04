package it.polimi.se2018;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.ArrayList;

/**
 * Tests for Server's class
 */
public class ServerTest {

    @Test
    public void testGetter() {
        Server server = Server.getInstance();

        Player p = new Player("Player");
        Game g = new Game();
        Game wrongG = new Game();

        //add player from online players test
        server.addPlayerToOnlinePlayers(p);
        Assert.assertEquals(3, server.getOnlinePlayers().size());
        Assert.assertNotEquals(5, server.getOnlinePlayers().size());

        //remove player from online players test
        server.removePlayerFromOnlinePlayers(p);
        Assert.assertTrue(!server.getOnlinePlayers().contains(p));
        Assert.assertTrue(server.getOfflinePlayers().contains(p));

        //current game getter test
        server.setCurrentGame(g);
        Assert.assertEquals(g, server.getCurrentGame());
        Assert.assertNotEquals(wrongG, server.getCurrentGame());

        //load configuration test
        Assert.assertEquals(5050, server.getServerPort());
        Assert.assertNotEquals(5040, server.getServerPort());
        Assert.assertEquals(1, server.getDefaultMatchmakingTimer());
        Assert.assertNotEquals(10, server.getDefaultMatchmakingTimer());
        Assert.assertEquals(1, server.getDefaultMoveTimer());
        Assert.assertNotEquals(10, server.getDefaultMoveTimer());
        Assert.assertEquals(false, server.isConfigurationRequired());
        Assert.assertNotEquals(true, server.isConfigurationRequired());
        Assert.assertEquals(3, server.getnOfTurn());
        Assert.assertNotEquals(0, server.getnOfTurn());
    }

}