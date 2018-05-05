package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.ToolCardEffectStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class Lathekin implements ToolCardEffectStrategy{

    private Dice dice1;
    private Dice dice2;
    private WindowPatternCard windowPatternCard;
    private WindowCell windowCell1;
    private WindowCell windowCell2;

    public Lathekin(Dice dice1, Dice dice2, WindowPatternCard windowPatternCard, WindowCell windowCell1, WindowCell windowCell2){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.windowPatternCard = windowPatternCard;
        this.windowCell1 = windowCell1;
        this.windowCell2 = windowCell2;
    }

    public Lathekin(Lathekin lathekin, Dice dice1, Dice dice2, WindowPatternCard windowPatternCard,  WindowCell windowCell1, WindowCell windowCell2){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.windowPatternCard = windowPatternCard;
        this.windowCell1 = windowCell1;
        this.windowCell2 = windowCell2;
    }

    @Override
    public int executeEffect() {
        int column1 = windowCell1.getColumn();
        int column2 = windowCell2.getColumn();
        int row1 = windowCell1.getRow();
        int row2 = windowCell2.getRow();

        windowPatternCard.getCell(column2, row2).setAssignedDice(windowPatternCard.getCell(column1, row1).getAssignedDice());

        Dice emptyDice = new Dice(0, null, null);
        windowPatternCard.getCell(column1, row2). setAssignedDice(emptyDice);
        
        return 0;
    }
}
