package it.polimi.se2018;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.controller.RoundHandler;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.net.CookieHandler;

public class GameControllerTest {

    @Test
    public void gameControllerTest(){
        Game game = new Game();
        GameController gameController = game.getAssociatedGameController();


        Player p1 = new Player("Alenichel");
        Player p2 = new Player("Stendus");

        ConnectionMessage cm = new ConnectionMessage(p1, true);
        ConnectionMessage cm2 = new ConnectionMessage(p2, true);

        gameController.timerDoneAction();


        Assert.assertEquals(90,game.getDiceBag().size()); //check if all dice have been initialized
        Assert.assertEquals(3, game.getObjectiveCards().size()); //check if all public objective card have been assigned
    }

    @Test
    public void testRoundHandler() throws GameException{
        Game g = new Game();
        GameController gc = g.getAssociatedGameController();
        Player p1 = new Player("Alenichel");
        Player p2 = new Player("Stendus");

        g.getAssociatedGameController().timerDoneAction();

        g.addPlayer(p1);
        g.addPlayer(p2);

        //RoundHandler rh = new RoundHandler(g);
    }
}
