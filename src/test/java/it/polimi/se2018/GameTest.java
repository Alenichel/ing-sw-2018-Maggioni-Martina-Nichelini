package it.polimi.se2018;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.enumeration.DiceColor;
import it.polimi.se2018.enumeration.ObjectiveCardsName;
import it.polimi.se2018.enumeration.WindowPatternCardsName;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests for Game's class
 */
public class GameTest {

    @Test
    public void testGetter() throws GameException {
        Player player1 = new Player("vale");
        Player player2 = new Player("ale");
        Player player3 = new Player("ste");
        Player wrongPlayer = new Player("was");

        Die die1 = new Die(DiceColor.red, 1);
        Die die2 = new Die(DiceColor.red, 2);
        Die die3 = new Die(DiceColor.red, 3);
        Die die4 = new Die(DiceColor.red, 4);
        Die die5 = new Die(DiceColor.red, 5);
        Die die6 = new Die(DiceColor.red, 6);

        PublicObjectiveCard objective1 = new PublicObjectiveCard(ObjectiveCardsName.RowColorVariety);
        PublicObjectiveCard objective2 = new PublicObjectiveCard(ObjectiveCardsName.LightShades);
        PublicObjectiveCard objective3 = new PublicObjectiveCard(ObjectiveCardsName.ColorVariety);

        ToolCard tc1 = new ToolCard(null);
        ToolCard tc2 = new ToolCard(null);
        ToolCard tc3 = new ToolCard(null);

        WindowPatternCard wpc1 = new WindowPatternCard(WindowPatternCardsName.virtus);
        WindowPatternCard wpc2 = new WindowPatternCard(WindowPatternCardsName.gravitas);
        WindowPatternCard wpc3 = new WindowPatternCard(WindowPatternCardsName.industria);

        ArrayList<Player> wrongPlayers = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Die> dice = new ArrayList<>();
        ArrayList<Die> wrongDie = new ArrayList<>();
        ArrayList<PublicObjectiveCard> objectives = new ArrayList<>();
        ArrayList<ToolCard> tc = new ArrayList<>();
        ArrayList<WindowPatternCard> wpc = new ArrayList<>();

        players.add(player1);
        players.add(player2);
        players.add(player3);

        wrongPlayers.add(player1);
        wrongPlayers.add(player2);
        wrongPlayers.add(player3);
        wrongPlayers.add(wrongPlayer);

        objectives.add(objective1);
        objectives.add(objective2);
        objectives.add(objective3);

        tc.add(tc1);
        tc.add(tc2);
        tc.add(tc3);

        wpc.add(wpc1);
        wpc.add(wpc2);
        wpc.add(wpc3);

        Game game = new Game();
        System.out.println(game.getName());

        //actual round getter test
        Assert.assertEquals(0, game.getActualRound());
        game.setActualRound(5);
        Assert.assertEquals(5, game.getActualRound());
        Assert.assertNotEquals(4, game.getActualRound());

        //active player getter test
        Assert.assertEquals(null, game.getActivePlayer());
        game.setActivePlayer(player1);
        Assert.assertEquals(player1, game.getActivePlayer());
        Assert.assertNotEquals(player2, game.getActivePlayer());

        //initialization complete getter
        game.setInitializationComplete(true);
        Assert.assertEquals(true, game.isInitializationComplete());

        //players getter test
        game.getPlayers().addAll(players);
        Assert.assertEquals(players, game.getPlayers());
        Assert.assertNotEquals(wrongPlayer, game.getPlayers());

        //remove player test
        players.remove(player1);
        game.removePlayer(player1);
        Assert.assertEquals(players, game.getPlayers());

        //timer seconds left getter test
        game.setTimerSecondLeft(30);
        Assert.assertEquals(30, game.getTimerSecondsLeft());

        //winner getter test
        game.setWinner(player2);
        Assert.assertEquals(player2, game.getWinner());


        // ------------------------------------------------------

        dice.add(die1);
        dice.add(die2);
        dice.add(die3);
        dice.add(die4);
        dice.add(die5);
        dice.add(die6);

        //Die bag getter test
        wrongDie = (ArrayList<Die>) dice.clone();
        game.setDiceBag(dice);
        Assert.assertEquals(dice, game.getDiceBag());

        //Die on table getter test
        game.setDiceOnTable(wrongDie);
        Assert.assertEquals(wrongDie, game.getDiceOnTable());

        //objective cards getter test
        game.setObjectiveCards(objectives);
        Assert.assertEquals(objectives, game.getObjectiveCards());
        Assert.assertNotEquals(tc, game.getObjectiveCards());

        //Tool card getter test
        game.setToolCards(tc);
        Assert.assertEquals(tc, game.getToolCards());
        Assert.assertNotEquals(objectives, game.getToolCards());

        //Pattern cards getter test
        game.setPatternCards(wpc);
        Assert.assertEquals(wpc, game.getPatternCards());
        Assert.assertNotEquals(tc, game.getPatternCards());

    }

}
