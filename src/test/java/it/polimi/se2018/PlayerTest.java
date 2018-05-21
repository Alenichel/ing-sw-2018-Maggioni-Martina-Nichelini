package it.polimi.se2018;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.Room;
import it.polimi.se2018.model.WindowPatternCard;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {
    @Test
    public void testGetter() {
        Player player = new Player("pippo");
        Room room = Room.getInstance();

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

    }

}
