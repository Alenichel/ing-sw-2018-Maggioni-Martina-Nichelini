package it.polimi.se2018;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.model.*;
import it.polimi.se2018.strategy.toolcard.GrozingPliers;
import it.polimi.se2018.strategy.toolcard.Lathekin;
import it.polimi.se2018.utils.DiceColor;
import it.polimi.se2018.utils.WindowPatternCardsName;
import static org.junit.Assert.*;
import it.polimi.se2018.strategy.toolcard.Lathekin;
import org.junit.Test;
import java.util.ArrayList;

public class ToolCardTest {

    @Test
    public void testLathekin(){
        WindowPatternCard windowPatternCard = new WindowPatternCard(WindowPatternCardsName.auroraeMagnificus);
        WindowCell wstart1 = windowPatternCard.getGrid()[0][0];
        WindowCell wstart2 = windowPatternCard.getGrid()[1][1];
        WindowCell wend1 = windowPatternCard.getGrid()[2][2];
        WindowCell wend2 = windowPatternCard.getGrid()[3][3];

        Dice diceStart1 = new Dice(DiceColor.Blue.toString());
        Dice diceStart2 = new Dice(DiceColor.Red.toString());
        try {
            wstart1.setAssignedDice(diceStart1);
            wstart1.setAssignedDice(diceStart2);
        }catch (NotEmptyWindowCellException e){
            fail();
        }
        Lathekin lathekin = new Lathekin();
        ToolCard toolCardProvaLathekin = new ToolCard(lathekin);

        lathekin = refactorLathekin(lathekin,windowPatternCard, wstart1, wstart2, wend1, wend2);

    }

}
