package it.polimi.se2018;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.WindowPatternCard;
import it.polimi.se2018.utils.WindowPatternCardsName;
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

        Assert.assertEquals("pippo", player.getNickname());


        PrivateObjectiveCard p = new PrivateObjectiveCard();

        player.setOnline(true);
        Assert.assertEquals(true, player.getOnline());
        Assert.assertNotEquals(false, player.getOnline());

        player.setPlayerNumber(2);
        Assert.assertEquals(2, player.getPlayerNumber());
        Assert.assertNotEquals(3, player.getPlayerNumber());

        player.setInGame(false);
        Assert.assertEquals(false, player.getInGame());
        Assert.assertNotEquals(true, player.getInGame());

        player.setActivePatternCard(windowPatternCard);
        Assert.assertEquals(windowPatternCard, player.getActivePatternCard());
        Assert.assertNotEquals(windowPatternCard2, player.getActivePatternCard());

    }

}
