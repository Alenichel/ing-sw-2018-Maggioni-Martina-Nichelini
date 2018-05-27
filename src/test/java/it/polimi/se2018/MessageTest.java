package it.polimi.se2018;

import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.message.SocketUpdateContainer;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.Observable;

public class MessageTest {

    @Test
    public void testControllerCallbackMessage(){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage("Callback");
        Assert.assertEquals(ccm.getStringMessage(), "Callback");
    }

    @Test
    public void testSocketUpdateContainer(){
        Object o2 = new Object();
        SocketUpdateContainer suc = new SocketUpdateContainer(null, o2);
        Assert.assertEquals(null, suc.getObservable());
        Assert.assertEquals(o2, suc.getObject());
    }

    @Test
    public void testSelectionMessage(){
        Player p1 =  new Player("Player");
        Object o = new Object();
        SelectionMessage sm = new SelectionMessage(o, p1);
        Assert.assertEquals(sm.getPlayer(), p1);
    }
}
