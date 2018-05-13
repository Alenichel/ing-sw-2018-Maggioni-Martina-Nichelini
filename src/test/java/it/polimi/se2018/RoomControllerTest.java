package it.polimi.se2018;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import it.polimi.se2018.message.RoomConnectionMessage;

public class RoomControllerTest {

    Player p;
    Player p2;
    Room r;

    @Before public void initialize() {
        this.p = new Player("Admin");
        this.p2 = new Player("player2");
        this.r = new Room("Roomname", p, false);
    }

    @Test
    public void testState(){

        RoomController rc = new RoomController(new RoomWaitingState(), this.r);
        Assert.assertEquals(true, rc.getState() instanceof RoomWaitingState);

        rc.update(null,new RoomConnectionMessage(this.p2, true));
        Assert.assertTrue(this.r.getListOfConnectedPlayers().contains(this.p2));

        rc.update(null,new RoomConnectionMessage(this.p2, false));
        Assert.assertFalse(this.r.getListOfConnectedPlayers().contains(this.p2));

        rc.setState(new RoomStartedState());
        Assert.assertEquals(true, rc.getState() instanceof RoomStartedState);

        try {
            rc.update(null,new RoomConnectionMessage(this.p2, true));
        } catch (Exception e){
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }
}
