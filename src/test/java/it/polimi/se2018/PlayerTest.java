package it.polimi.se2018;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.WindowPatternCardsName;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {
    @Test
    public void testGetter() {
        Player player = new Player("pippo");

        WindowPatternCard windowPatternCard = new WindowPatternCard(WindowPatternCardsName.viaLux);
        WindowPatternCard windowPatternCard2 = new WindowPatternCard(WindowPatternCardsName.virtus);
        WindowPatternCard windowPatternCard3 = new WindowPatternCard(WindowPatternCardsName.sunCatcher);
        WindowPatternCard windowPatternCard4 = new WindowPatternCard(WindowPatternCardsName.firmitas);

        ArrayList<WindowPatternCard> windowPatternCards = new ArrayList<>();
        windowPatternCards.add(windowPatternCard);
        windowPatternCards.add(windowPatternCard2);
        windowPatternCards.add(windowPatternCard3);
        windowPatternCards.add(windowPatternCard4);

        Game game = new Game();
        Game wrongGame = new Game();

        //last game joined getter test
        player.setLastGameJoined(game);
        Assert.assertEquals(game, player.getLastGameJoined());
        Assert.assertNotEquals(wrongGame, player.getLastGameJoined());

        //score getter test
        player.setScore(300);
        Assert.assertEquals(300, player.getScore());
        Assert.assertNotEquals(3200, player.getScore());

        //nickname getter test
        Assert.assertEquals("pippo", player.getNickname());
        Assert.assertNotEquals("pioppo", player.getNickname());

        //online getter test
        player.setOnline(true);
        Assert.assertEquals(true, player.getOnline());
        Assert.assertNotEquals(false, player.getOnline());

        //player number getter test
        player.setPlayerNumber(2);
        Assert.assertEquals(2, player.getPlayerNumber());
        Assert.assertNotEquals(3, player.getPlayerNumber());

        //in game getter test
        player.setInGame(false);
        Assert.assertEquals(false, player.getInGame());
        Assert.assertNotEquals(true, player.getInGame());

        //active window pattern card getter test
        player.setActivePatternCard(windowPatternCard);
        Assert.assertEquals(windowPatternCard, player.getActivePatternCard());
        Assert.assertNotEquals(windowPatternCard2, player.getActivePatternCard());

        //window pattern cards pool getter test
        player.setWindowPatternCardsPool(windowPatternCards);
        Assert.assertNotEquals(null, player.getWindowPatternCardsPool());
        Assert.assertEquals(windowPatternCards, player.getWindowPatternCardsPool());

        //toString test
        Assert.assertEquals("pippo", player.toString());
        Assert.assertNotEquals("pioppo", player.toString());

        //name getter test
        WindowPatternCard w = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        player.setInGame(true);
        player.assignPatternCard(w);
        Assert.assertEquals(player.getActivePatternCard().getWPName(), w.getWPName());
    }

}
