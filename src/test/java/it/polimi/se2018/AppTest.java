package it.polimi.se2018;

import static org.junit.Assert.assertTrue;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.CliView;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    public static void main(String[] args) {
        Player a = new Player("adminName");
        Player p = new Player("Alenichel");

        CliView cw = new CliView(p);
        cw.run();
    }
}
