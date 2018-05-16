package it.polimi.se2018;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ServerTest {

    @Test
    public void testGetter() {
        Server s = new Server();
        String gameName = "game";
        Player p = new Player("admin");
        Room r = new Room("room", p, false);

        Assert.assertEquals( 5050, s.getServerPort());
        Assert.assertNotEquals(2018, s.getServerPort());

        Assert.assertEquals( 120, s.getDefaultMatchmakingTimer());
        Assert.assertNotEquals(100, s.getDefaultMatchmakingTimer());

        Assert.assertEquals(60, s.getDefaultMoveTimer());
        Assert.assertNotEquals(90, s.getDefaultMoveTimer());

        s.addPlayer(p);
        Assert.assertTrue(s.getOnlinePlayers().contains(p));

        s.removePlayer(p);
        Assert.assertTrue(!s.getOnlinePlayers().contains(p));

        Room room = new Room(gameName, p, false);
        s.addRoom(room);
        Assert.assertEquals(true, s.getActiveGames().contains(r));

        /*removeRoom(gameName, p);
        Assert.assertEquals(true, !s.getActiveGames().contains(r));*/

    }

}