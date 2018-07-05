package it.polimi.se2018;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.enumeration.WindowPatternCardsName;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.MoveDiceMessage;
import it.polimi.se2018.message.SelectionMessage;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.view.CliView;
import it.polimi.se2018.view.VirtualView;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Server controller
 */
public class ControllerTest {

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

            while (true) {
                while (container == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                    }
                }

                if (container instanceof ConnectionMessage) ServerController.getInstance().update(vv, container);
                else if (container instanceof SelectionMessage || container instanceof MoveDiceMessage) {
                    System.out.println("select");
                    try {
                        Server.getInstance().getCurrentGame().getAssociatedGameController().update(vv, container);
                    }
                    catch (NullPointerException e){
                        Server.getInstance().getActiveGames().get(0).getAssociatedGameController().update(vv, container);
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

        Server server = Server.getInstance();
        server.setDefaultMatchmakingTimer(1);
        server.setDefaultMoveTimer(1);
        server.setnOfTurn(5);
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


        try {
            Thread.sleep((Server.getInstance().getDefaultMatchmakingTimer() + 6) * 1000 );
            //Thread.sleep(20000 );
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }

        //-----------------------------------------------PARTITA INIZIATA, ORA SELEZIONIAMO PATTERNCARD-------------------------------------------
        Assert.assertTrue(Server.getInstance().getOnlinePlayers().contains(p1));
        Assert.assertTrue(Server.getInstance().getOnlinePlayers().contains(p2));
        //Assert.assertEquals(2, Server.getInstance().getOnlinePlayers().size());
        //Assert.assertEquals(true, Server.getInstance().getActiveGames().get(0).isStarted());

        SelectionMessage sm1 = new SelectionMessage(0, p1,"PatternCard");
        SelectionMessage sm2 = new SelectionMessage(0, p2,"PatternCard");
        h1.setContainer(sm1);
        h2.setContainer(sm2);

        p1.setActivePatternCard(new WindowPatternCard(WindowPatternCardsName.batllo));
        p2.setActivePatternCard(new WindowPatternCard(WindowPatternCardsName.luxAstram));

        //-----------------------------------------------PARTITA REALMENTE INIZIATA------------------------------------------------------------

        try {
            Thread.sleep((Server.getInstance().getnOfTurn() * 2 * Server.getInstance().getDefaultMoveTimer() + 1) * 1000 );
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }

        Game game = Server.getInstance().getActiveGames().get(0);

        if(game.getActivePlayer().equals(p1)){
            //turno di p1
            MoveDiceMessage mdm1 = new MoveDiceMessage(1, 1, 1);
            h1.setContainer(mdm1);
        }
        else if(game.getActivePlayer().equals(p2)){
            //turno di p2
            MoveDiceMessage mdm2 = new MoveDiceMessage(2, 1, 1);
            h2.setContainer(mdm2);
        }


        try {
            Thread.sleep((Server.getInstance().getnOfTurn() * 2 * Server.getInstance().getDefaultMoveTimer() + 1) * 1000 );
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }

        h1.stop();
        h2.stop();
    }

    private void waitATurn(){
        try {
            Thread.sleep((Server.getInstance().getDefaultMoveTimer()+1)*1000);
        }catch (InterruptedException e){
            ;
        }

    }
}
