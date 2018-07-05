package it.polimi.se2018;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Tests for Messages
 */
public class MessageTest {

    @Test
    // test for connection message
    public void testConnectionMessage(){
        Player requester =  new Player("Player");
        boolean isConnecting = true;
        ConnectionMessage cm = new ConnectionMessage(requester, isConnecting);
        Assert.assertEquals(cm.getRequester(), requester);
        Assert.assertEquals(true, cm.isConnecting());
    }

    @Test
    // test for controller callback message
    public void testControllerCallbackMessage(){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage("Callback", LoggerPriority.NORMAL);
        Assert.assertEquals(ccm.getStringMessage(), "Callback");
    }

    @Test
    // test for give message
    public void testGiveMessage(){
        String s = "s";
        Object o = new Object();
        GiveMessage gm = new GiveMessage(s, o);
        Assert.assertEquals(gm.getGivenObject(), o);
        Assert.assertEquals(gm.getGiving(), s);
    }

    @Test
    // test for handshake connection message
    public void testHandshakeConnectionMessage(){
        Player p1 =  new Player("Player");
        String username = "bob";
        String password = "bob11";
        HandshakeConnectionMessage hcm = new HandshakeConnectionMessage(username, password);
        HandshakeConnectionMessage hcm2 = new HandshakeConnectionMessage(p1);
        Assert.assertEquals(hcm.getUsername(), username);
        Assert.assertEquals(hcm.getEncodedPassword(), password);
        Assert.assertEquals(hcm2.getPlayer(), p1);
    }

    @Test
    // test for move dice message
    public void testMoveDiceMessage(){
        int tc = 2;
        int eX = 2;
        int eY = 2;
        MoveDiceMessage mdm = new MoveDiceMessage(tc, eX, eY);
        Assert.assertEquals(mdm.getTableCoordinate(), tc - 1);
        Assert.assertEquals(mdm.getEndingX(), eX);
        Assert.assertEquals(mdm.getEndingY(), eY);
        Assert.assertEquals(DiceLocation.TABLE, mdm.getStartingLocation());
        Assert.assertEquals( DiceLocation.WINDOWCELL, mdm.getEndingLocation());
    }

    @Test
    // test for request message
    public void testRequestMessage(){
        String request = "request";
        RequestMessage rm = new RequestMessage(request);
        Assert.assertEquals(rm.getRequest(), request);
    }

    @Test
    // test for room connection message
    public void testRoomConnectionMessage(){
        Player requester =  new Player("Player");
        boolean isConnecting = true;
        RoomConnectionMessage rcm = new RoomConnectionMessage(requester, isConnecting);
        Assert.assertEquals(rcm.getRequester(), requester);
        Assert.assertEquals(true, rcm.isConnecting());
    }

    @Test
    // test for selection message
    public void testSelectionMessage(){
        Player p1 =  new Player("Player");
        Object o = new Object();
        SelectionMessage sm = new SelectionMessage(o, p1);
        Assert.assertEquals(sm.getPlayer(), p1);
        Assert.assertEquals(sm.getChosenItem(), o);
    }

    @Test
    // test for socket update container
    public void testSocketUpdateContainer(){
        Object o2 = new Object();
        SocketUpdateContainer suc = new SocketUpdateContainer(null, o2);
        Assert.assertEquals(null, suc.getObservable());
        Assert.assertEquals(o2, suc.getObject());
    }
}
