package it.polimi.se2018;

import it.polimi.se2018.controller.GameController;
import it.polimi.se2018.model.Game;
import org.junit.Assert;
import org.junit.Test;

public class GameControllerTest {

    @Test
    public void gameControllerTest(){
        Game game = new Game();
        GameController gameController = game.getAssociatedGameController();
        gameController.timerDoneAction();

        Assert.assertEquals(90,game.getDiceBag().size()); //check if all dice have been initialized
        Assert.assertEquals(3, game.getObjectiveCards().size()); //check if all public objective card have been assigned
    }
}
