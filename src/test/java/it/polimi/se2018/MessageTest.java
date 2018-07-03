package it.polimi.se2018;

import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.message.SocketUpdateContainer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.enumeration.LoggerPriority;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Messages
 */
public class MessageTest {

    @Test
    // test for controller callback message
    public void testControllerCallbackMessage(){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage("Callback", LoggerPriority.NORMAL);
        Assert.assertEquals(ccm.getStringMessage(), "Callback");
    }

    @Test
    // test for socket update container
    public void testSocketUpdateContainer(){
        Object o2 = new Object();
        SocketUpdateContainer suc = new SocketUpdateContainer(null, o2);
        Assert.assertEquals(null, suc.getObservable());
        Assert.assertEquals(o2, suc.getObject());
    }

    @Test
    // test for selection message
    public void testSelectionMessage(){
        Player p1 =  new Player("Player");
        Object o = new Object();
        SelectionMessage sm = new SelectionMessage(o, p1);
        Assert.assertEquals(sm.getPlayer(), p1);
    }
}
