package it.polimi.se2018;

import org.junit.Assert;
import org.junit.Test;

public class RoomTest {

    @Test
    public void testGetter(){
        String name = "stanza";
        Player p = new Player("admin");
        Player p2 = new Player("notAdmin");

        Room r = new Room(name, p, false);

        Assert.assertEquals(name, r.getRoomName());
        Assert.assertTrue(r.getListOfConnectedPlayers().contains(p));
        Assert.assertFalse(r.isADisconnectedClient(p));

        r.addPlayer(p2);
        Assert.assertTrue(r.getListOfConnectedPlayers().contains(p2));

        r.removePlayer(p2);
        r.addDisconnectedClient(p2);
        Assert.assertTrue(!r.getListOfConnectedPlayers().contains(p2));
        Assert.assertTrue(r.getListOfDisconnectedClients().contains(p2));
    }
}
