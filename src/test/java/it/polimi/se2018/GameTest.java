package it.polimi.se2018;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.ObjectiveCardsName;
import it.polimi.se2018.utils.WindowPatternCardsName;
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

        Dice die1 = new Dice(DiceColor.red, 1);
        Dice die2 = new Dice(DiceColor.red, 2);
        Dice die3 = new Dice(DiceColor.red, 3);
        Dice die4 = new Dice(DiceColor.red, 4);
        Dice die5 = new Dice(DiceColor.red, 5);
        Dice die6 = new Dice(DiceColor.red, 6);

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
        ArrayList<Dice> dice = new ArrayList<>();
        ArrayList<Dice> wrongDice = new ArrayList<>();
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
