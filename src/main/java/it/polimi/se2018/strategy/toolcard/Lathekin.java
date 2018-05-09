package it.polimi.se2018.strategy.toolcard;

import it.polimi.se2018.Dice;
import it.polimi.se2018.Exception.EmptyWindowCellException;
import it.polimi.se2018.Exception.NotEmptyWindowCellException;
import it.polimi.se2018.Exception.ToolCardException;
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
        //this.windowPatternCard = windowPatternCard;
        //this.windowCellStart1 = windowCellStart1;
        //this.windowCellArrive1 = windowCellArrive1;
        //this.windowCellStart2 = windowCellStart2;
        //this.windowCellArrive2 = windowCellArrive2;

        refactorLathekin(this, windowPatternCard, windowCellStart1, windowCellArrive1, windowCellStart2, windowCellArrive2);
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
    public int executeEffect() throws ToolCardException {
        Dice emptyDice = new Dice("red", "nowhere");

        int startColumn1 = windowCellStart1.getColumn();
        int arriveColumn1 = windowCellArrive1.getColumn();
        int startRow1 = windowCellStart1.getRow();
        int arriveRow1 = windowCellArrive1.getRow();
        int startColumn2 = windowCellStart2.getColumn();
        int arriveColumn2 = windowCellArrive2.getColumn();
        int startRow2 = windowCellStart2.getRow();
        int arriveRow2 = windowCellArrive2.getRow();


        //Check if the destination of the dices is free
            if(!windowCellArrive1.isEmpty()){ throw new NotEmptyWindowCellException("windows cell 1 is not empty"); }
            if(!windowCellArrive2.isEmpty()){ throw new NotEmptyWindowCellException("windows cell 2 is not empty"); }


        //Check if the source of the dices is notEmpty
            if(windowCellStart1.isEmpty()){ throw new EmptyWindowCellException("windows cell 1 is empty"); }
            if(windowCellStart2.isEmpty()){ throw new EmptyWindowCellException("windows cell 2 is empty"); }

        //bisogna controllare i vincoli
        windowPatternCard.getCell(arriveColumn1, arriveRow1).setAssignedDice(windowPatternCard.getCell(startColumn1, startRow1).getAssignedDice());
        windowPatternCard.getCell(startColumn1, arriveRow1). setAssignedDice(emptyDice);

        windowPatternCard.getCell(arriveColumn2, arriveRow2).setAssignedDice(windowPatternCard.getCell(startColumn2, startRow2).getAssignedDice());
        windowPatternCard.getCell(startColumn2, arriveRow2). setAssignedDice(emptyDice);



        return 0;
    }
}
