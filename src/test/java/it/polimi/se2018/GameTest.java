package it.polimi.se2018;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GameTest {

    @Test
    public void testGetter() throws GameException {
        Player player1 = new Player("vale");
        Player player2 = new Player("ale");
        Player player3 = new Player("ste");
        Player wrongPlayer = new Player("was");

        Dice die1 = new Dice("red", 1);
        Dice die2 = new Dice("red", 2);
        Dice die3 = new Dice("red", 3);
        Dice die4 = new Dice("red", 4);
        Dice die5 = new Dice("red", 5);
        Dice die6 = new Dice("red", 6);
        Dice wrongDie = new Dice("pink", 7);

        ObjectiveCard objective1 = new ObjectiveCard();
        ObjectiveCard objective2 = new ObjectiveCard();
        ObjectiveCard objective3 = new ObjectiveCard();

        ToolCard tc1 = new ToolCard(null);
        ToolCard tc2 = new ToolCard(null);
        ToolCard tc3 = new ToolCard(null);

        WindowPatternCard wpc1 = new WindowPatternCard("virtus");
        WindowPatternCard wpc2 = new WindowPatternCard("gravitas");
        WindowPatternCard wpc3 = new WindowPatternCard("industria");

        ArrayList<Player> wrongPlayers = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Dice> dice = new ArrayList<>();
        ArrayList<Dice> wrongDice = new ArrayList<>();
        ArrayList<ObjectiveCard> objectives = new ArrayList<>();
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

        //getPlayers test
        game.getPlayers().addAll(players);
        Assert.assertEquals(players, game.getPlayers());
        Assert.assertNotEquals(wrongPlayer, game.getPlayers());

        dice.add(die1);
        dice.add(die2);
        dice.add(die3);
        dice.add(die4);
        dice.add(die5);
        dice.add(die6);

        //getDiceBag test
        wrongDice = (ArrayList<Dice>) dice.clone();
        wrongDice.add(wrongDie);
        game.setDiceBag(dice);
        Assert.assertEquals(dice, game.getDiceBag());
        Assert.assertNotEquals(wrongDice, game.getDiceBag());

        //getDiceOnTable test
        game.setDiceOnTable(wrongDice);
        Assert.assertEquals(wrongDice, game.getDiceOnTable());
        Assert.assertNotEquals(dice, game.getDiceOnTable());

        //getObjectiveCards test
        game.setObjectiveCards(objectives);
        Assert.assertEquals(objectives, game.getObjectiveCards());
        Assert.assertNotEquals(tc, game.getObjectiveCards());

        //getToolCards test
        game.setToolCards(tc);
        Assert.assertEquals(tc, game.getToolCards());
        Assert.assertNotEquals(objectives, game.getToolCards());

        //getPatternCards test
        game.setPatternCards(wpc);
        Assert.assertEquals(wpc, game.getPatternCards());
        Assert.assertNotEquals(tc, game.getPatternCards());

    }
}
