package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PlayerTest {
    @Test
    public void testGetter() {
        Player player = new Player("pippo");
        Room room = new Room("paperino", player, false);
        Room room2 = new Room("minnie", player, false);

        WindowPatternCard windowPatternCard = new WindowPatternCard("viaLux.xml");


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
        player.setInGame(false);
        //Assert.assertEquals(windowPatternCard, player.getActivePatternCard());



    }

}
