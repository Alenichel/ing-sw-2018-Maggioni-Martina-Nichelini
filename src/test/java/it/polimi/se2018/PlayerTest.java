package it.polimi.se2018;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {
    @Test
    public void testGetter() {
        Player player = new Player("pippo");
        Room room = new Room("paperino", player, false);
        Room room2 = new Room("minnie", player, false);

        WindowPatternCard windowPatternCard = new WindowPatternCard("viaLux");
        WindowPatternCard windowPatternCard2 = new WindowPatternCard("virtus");
        WindowPatternCard windowPatternCard3 = new WindowPatternCard("sunCatcher");
        WindowPatternCard windowPatternCard4 = new WindowPatternCard("firmitas");

        ArrayList<WindowPatternCard> windowPatternCards = new ArrayList<>();
        windowPatternCards.add(windowPatternCard);
        windowPatternCards.add(windowPatternCard2);
        windowPatternCards.add(windowPatternCard3);
        windowPatternCards.add(windowPatternCard4);

        Assert.assertEquals("pippo", player.getNickname());
        Assert.assertNotEquals("pluto", player.getNickname());

        //non ancora fatto
        PrivateObjectiveCard p = new PrivateObjectiveCard();

        player.setOnline(true);
        Assert.assertEquals(true, player.getOnline());
        Assert.assertNotEquals(false, player.getOnline());

        player.setPlayerNumber(2);
        Assert.assertEquals(2, player.getPlayerNumber());
        Assert.assertNotEquals(3, player.getPlayerNumber());

        player.setLastGameJoined(room);
        Assert.assertEquals(room, player.getLastGameJoined());
        Assert.assertNotEquals(room2, player.getLastGameJoined());

        player.setInGame(false);
        Assert.assertEquals(false, player.getInGame());
        Assert.assertNotEquals(true, player.getInGame());

        //player.assignPatternCard(windowPatternCard);
        //player.setInGame(false);
        //Assert.assertEquals(windowPatternCard, player.getActivePatternCard());

        player.setActivePatternCard(windowPatternCard);
        Assert.assertEquals(windowPatternCard, player.getActivePatternCard());


    }

}
