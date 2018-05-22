package it.polimi.se2018;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;


public class ServerTest {

    @Test
    public void testGetter() {
        Server s = Server.getInstance();
        Player p = new Player("admin");
        ArrayList<Player> onlinePlayers = new ArrayList<>();
        ArrayList<Player> inGamePlayers = new ArrayList<>();
        ArrayList<Player> waitingPlayers = new ArrayList<>();

        Assert.assertEquals( 5050, s.getServerPort());
        Assert.assertNotEquals(2018, s.getServerPort());

        Assert.assertEquals( 120, s.getDefaultMatchmakingTimer());
        Assert.assertNotEquals(100, s.getDefaultMatchmakingTimer());

        Assert.assertEquals(60, s.getDefaultMoveTimer());
        Assert.assertNotEquals(90, s.getDefaultMoveTimer());

        Assert.assertEquals(null, s.getCurrentGame());
        Assert.assertNotEquals("g", s.getCurrentGame());

        s.getOnlinePlayers().add(p);
        s.addPlayer(onlinePlayers, p);
        Assert.assertTrue(s.getOnlinePlayers().contains(p));

        s.getOnlinePlayers().remove(p);
        s.removePlayer(onlinePlayers, p);
        Assert.assertTrue(!s.getOnlinePlayers().contains(p));

        s.getWaitingPlayers().add(p);
        s.addPlayer(waitingPlayers, p);
        Assert.assertTrue(s.getWaitingPlayers().contains(p));

        s.getWaitingPlayers().remove(p);
        s.removePlayer(waitingPlayers, p);
        Assert.assertTrue(!s.getWaitingPlayers().contains(p));

        s.getInGamePlayers().add(p);
        s.addPlayer(inGamePlayers, p);
        Assert.assertTrue(s.getInGamePlayers().contains(p));

        s.getInGamePlayers().remove(p);
        s.removePlayer(inGamePlayers, p);
        Assert.assertTrue(!s.getInGamePlayers().contains(p));

    }

}