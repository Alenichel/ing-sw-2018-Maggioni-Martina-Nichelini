package it.polimi.se2018;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.MoveDiceMessage;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.VirtualView;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Server controller
 */
public class ControllerTest {

    int flag = 0;

    private class Handler extends Thread{

        VirtualView vv ;
        Object container ;

        public Handler(VirtualView vv, Object container){
            this.vv = vv;
            this.container = container;
        }

        public void setContainer(Object o){
            container = o;
        }

        @Override
        public void run () {

            boolean isConnecting = true;

            while (true) {
                while (container == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                    }
                }

                if (container instanceof ConnectionMessage && isConnecting) {
                    ServerController.getInstance().update(vv, container);
                    isConnecting = false;
                }
                else if (container instanceof SelectionMessage ||
                        container instanceof ConnectionMessage ||
                        container instanceof MoveDiceMessage ||
                        container instanceof UpdateMessage) {
                    System.out.println("select");
                    try {
                        Server.getInstance().getCurrentGame().getAssociatedGameController().update(vv, container);
                    }
                    catch (NullPointerException e){
                        try {
                            Server.getInstance().getActiveGames().get(0).getAssociatedGameController().update(vv, container);
                        } catch (NullPointerException e2) {
                            flag = 1;
                        }
                    }

                }
                container = null;
            }
        }

    }

    /**
     * This test aim to cover all controllers fuction.
     * The idea is to close MVC pattern without the network layer and simulate an entire game, from the beginning, to the end.
     */
    @Test
    public void controllerTest() {

        Logger.setSide(LoggerType.CLIENT_SIDE, false );

        Server server = Server.getInstance();
        server.setDefaultMatchmakingTimer(5);
        server.setDefaultMoveTimer(10);
        server.setnOfTurn(1);
        ServerController serverController = ServerController.getInstance();

        Player p1 = new Player("Alenichel");
        Object container = null;
        CliView cw1 = new CliView();
        cw1.setPlayer(p1);
        VirtualView vv1 = new VirtualView(cw1, p1);
        p1.setVv(vv1);
        Handler h1 = new Handler(vv1, container);
        h1.start();

        ConnectionMessage cm = new ConnectionMessage(p1, true);
        h1.setContainer(cm);

        Player p2 = new Player("Stendus");
        Object container2 = null;
        CliView cw2 = new CliView();
        cw2.setPlayer(p2);
        VirtualView vv2 = new VirtualView(cw2, p2);
        p2.setVv(vv2);
        Handler h2 = new Handler(vv2, container2);
        h2.start();

        ConnectionMessage cm2 = new ConnectionMessage(p2, true);
        h2.setContainer(cm2);


        Player p3 = new Player("Valentina");
        Object container3 = null;
        CliView cw3 = new CliView();
        cw3.setPlayer(p3);
        VirtualView vv3 = new VirtualView(cw3, p3);
        p3.setVv(vv3);
        Handler h3 = new Handler(vv3, container3);
        h3.start();

        ConnectionMessage cm3 = new ConnectionMessage(p3, true);
        h3.setContainer(cm3);


        try {
            Thread.sleep((Server.getInstance().getDefaultMatchmakingTimer() + 5) * 1000 );
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }
        Assert.assertTrue(Server.getInstance().getOnlinePlayers().contains(p1));
        Assert.assertTrue(Server.getInstance().getOnlinePlayers().contains(p2));


        SelectionMessage sm1 = new SelectionMessage(0, p1,"PatternCard");
        SelectionMessage sm2 = new SelectionMessage(0, p2,"PatternCard");
        SelectionMessage sm3 = new SelectionMessage(0, p3,"PatternCard");
        h1.setContainer(sm1);
        h2.setContainer(sm2);
        h3.setContainer(sm3);

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }


        while (flag != 1){
                h1.setContainer(new UpdateMessage(WhatToUpdate.Pass));
                try {
                    MoveDiceMessage mdm3 = new MoveDiceMessage(1, 0, 0);
                    h3.setContainer(mdm3);
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
                h2.setContainer(new UpdateMessage(WhatToUpdate.Pass));
                try {
                    MoveDiceMessage mdm1 = new MoveDiceMessage(1, 0, 0);
                    h1.setContainer(mdm1);
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
                h3.setContainer(new UpdateMessage(WhatToUpdate.Pass));
                try {
                    MoveDiceMessage mdm2 = new MoveDiceMessage(1, 0, 0);
                    h2.setContainer(mdm2);
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
        }

        h1.stop();
        h2.stop();
        h3.stop();
        return;
    }


}