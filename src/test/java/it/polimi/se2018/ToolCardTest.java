package it.polimi.se2018;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.strategy.toolcard.GrozingPliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ToolCardTest {
    @Test
    public void testGetter(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        players.add(new Player("paperino"));

        ArrayList<Player> wrongPlayers = (ArrayList<Player>) players.clone();
        wrongPlayers.add(new Player("wrong player"));
        Game game = new Game(players);
        Game wrongGame = new Game(wrongPlayers);

        Dice dice = new Dice("green");
        Dice dice2 = new Dice("pink");

        GrozingPliers grozingPliers = new GrozingPliers(dice, true);
        GrozingPliers grozingPliers2 = new GrozingPliers(dice2, true);

        ToolCard toolCard = new ToolCard(grozingPliers);

        toolCard.setUsed(true);
        Assert.assertTrue(toolCard.getUsed());
        Assert.assertNotEquals(false, toolCard.getUsed());
        Assert.assertTrue(toolCard.isUsed());


        toolCard.setDiceColor("green");
        Assert.assertEquals("green", toolCard.getDiceColor());
        Assert.assertNotEquals("pink", toolCard.getDiceColor());

        toolCard.setDescription("carta bella");
        Assert.assertEquals("carta bella", toolCard.getDescription());
        Assert.assertNotEquals("carta brutta", toolCard.getDescription());

        toolCard.setToolCardEffect(grozingPliers);
        Assert.assertEquals(grozingPliers, toolCard.getToolCardEffect());
        Assert.assertNotEquals(grozingPliers2, toolCard.getToolCardEffect());

        toolCard.setGameReference(game);
        Assert.assertEquals(game, toolCard.getGameReference());
        Assert.assertNotEquals(wrongGame, toolCard.getGameReference());
    }

}
