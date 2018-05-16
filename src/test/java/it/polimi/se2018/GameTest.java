package it.polimi.se2018;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
public class GameTest {
    @Test
    public void testGetter(){
        Player player1 = new Player("vale");
        Player player2 = new Player("ale");
        Player player3 = new Player("ste");
        Player wrongPlayer = new Player("was");

        Dice die1 = new Dice("red");
        Dice die2 = new Dice("red");
        Dice die3 = new Dice("red");
        Dice die4 = new Dice("red");
        Dice die5 = new Dice("red");
        Dice die6 = new Dice("red");
        Dice wrongDie = new Dice("pink");

        ArrayList<Player> wrongPlayers = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Dice> dice = new ArrayList<>();
        ArrayList<Dice> wrongDice = new ArrayList<>();


        players.add(player1);
        players.add(player2);
        players.add(player3);

        wrongPlayers.add(player1);
        wrongPlayers.add(player2);
        wrongPlayers.add(player3);
        wrongPlayers.add(wrongPlayer);

        Game game = new Game(players);

        Assert.assertEquals(players, game.getPlayers());
        Assert.assertNotEquals(wrongPlayer, game.getPlayers());

        dice.add(die1);
        dice.add(die2);
        dice.add(die3);
        dice.add(die4);
        dice.add(die5);
        dice.add(die6);

        wrongDice = (ArrayList<Dice>) dice.clone();
        wrongDice.add(wrongDie);
        game.setDiceBag(dice);
        Assert.assertEquals(dice, game.getDiceBag());
        Assert.assertNotEquals(wrongDice, game.getDiceBag());

        game.setDiceOnTable(wrongDice);
        Assert.assertEquals(wrongDice, game.getDiceOnTable());
        Assert.assertNotEquals(dice, game.getDiceOnTable());




    }
}
