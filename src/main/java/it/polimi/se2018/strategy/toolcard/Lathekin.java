package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.ToolCardEffectStrategy;
import it.polimi.se2018.WindowCell;
import it.polimi.se2018.WindowPatternCard;

public class Lathekin implements ToolCardEffectStrategy{


    private WindowPatternCard windowPatternCard;
    //Dice 1
    private WindowCell windowCellStart1;
    private WindowCell windowCellArrive1;
    //Dice 2
    private WindowCell windowCellStart2;
    private WindowCell windowCellArrive2;

    public Lathekin(WindowPatternCard windowPatternCard, WindowCell windowCellStart1, WindowCell windowCellArrive1, WindowCell windowCellStart2, WindowCell windowCellArrive2){
        this.windowPatternCard = windowPatternCard;
        this.windowCellStart1 = windowCellStart1;
        this.windowCellArrive1 = windowCellArrive1;
        this.windowCellStart2 = windowCellStart2;
        this.windowCellArrive2 = windowCellArrive2;
    }

    public Lathekin refactorLathekin(Lathekin lathekin, WindowPatternCard windowPatternCard, WindowCell windowCellStart1, WindowCell windowCellArrive1,  WindowCell windowCellStart2, WindowCell windowCellArrive2){
        lathekin.windowPatternCard = windowPatternCard;
        lathekin.windowCellStart1 = windowCellStart1;
        lathekin.windowCellArrive1 = windowCellArrive1;
        lathekin.windowCellStart2 = windowCellStart2;
        lathekin.windowCellArrive2 = windowCellArrive2;
        return lathekin;
    }

    @Override
    public int executeEffect() {
        Dice emptyDice = new Dice(0, null, null);

        int startColumn1 = windowCellStart1.getColumn();
        int arriveColumn1 = windowCellArrive1.getColumn();
        int startRow1 = windowCellStart1.getRow();
        int arriveRow1 = windowCellArrive1.getRow();
        int startColumn2 = windowCellStart2.getColumn();
        int arriveColumn2 = windowCellArrive2.getColumn();
        int startRow2 = windowCellStart2.getRow();
        int arriveRow2 = windowCellArrive2.getRow();


        //Probabilmente dobbiamo richiamare il metodo insertDice in Windows Pattern al posto di setAssignedDice di WindowCell in modo da poter controllare ad esempio che la destinazione non sia occupata
        //Check if the destination of the dices is free
        if(windowCellArrive1.getAssignedDice() != null || windowCellArrive2.getAssignedDice()!=null){
            return 1;
        }
        windowPatternCard.getCell(arriveColumn1, arriveRow1).setAssignedDice(windowPatternCard.getCell(startColumn1, startRow1).getAssignedDice());
        windowPatternCard.getCell(startColumn1, arriveRow1). setAssignedDice(emptyDice);

        windowPatternCard.getCell(arriveColumn2, arriveRow2).setAssignedDice(windowPatternCard.getCell(startColumn2, startRow2).getAssignedDice());
        windowPatternCard.getCell(startColumn2, arriveRow2). setAssignedDice(emptyDice);



        return 0;
    }
}
