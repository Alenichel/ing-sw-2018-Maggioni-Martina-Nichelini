package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.enumeration.CallbackMessageSubject;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import it.polimi.se2018.view.VirtualView;

import java.util.HashMap;


public class ToolCardController {

    private Game gameAssociated;

    protected ToolCardController(Game gameAssociated){
        this.gameAssociated = gameAssociated;
    }

    private void onFailure(VirtualView vv, String errorMessage){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.ToolcardNack , errorMessage, LoggerPriority.NOTIFICATION);
        vv.controllerCallback(ccm);
    }

    private void onSuccess(VirtualView vv){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.ToolCardAck ,LoggerPriority.NOTIFICATION);
        vv.controllerCallback(ccm);
    }

    private void handleGrozingPliers(HashMap<ToolcardContent, Object> params) throws ToolCardException{
        int tableDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice tableDie = this.gameAssociated.getDiceOnTable().get(tableDieIndex);
        Boolean increase = (boolean) params.get(ToolcardContent.Increase);


        if (increase){
            if (tableDie.getNumber() != 6) tableDie.setNumber(tableDie.getNumber() + 1);
            else throw new ToolCardException(":GROZING_PLIERS: you cannot change up a die with a six");
        }

        else {
            if (tableDie.getNumber() != 1) tableDie.setNumber(tableDie.getNumber() - 1);
            else throw new ToolCardException(":GROZING_PLIERS: you cannot change down a die with a one");
        }
    }

    private void handleEnglomiseBrush(HashMap<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException{
        WindowPatternCard windowPatternCard = (WindowPatternCard) params.get(ToolcardContent.WindowPattern);

        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);

        WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);
        WindowCell end = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);


        if(start.isEmpty())
            throw new ToolCardException(":ENGLOMISE_BRUSH: this window cell is empty");

        if(!end.isEmpty())
            throw new ToolCardException(":ENGLOMISE_BRUSH: this window cell is not empty");


        Dice d1 = start.getAssignedDice();
        try {
            start.removeDice();
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            start.setAssignedDice(d1);
            throw e;
        }
    }

    private  void handleCopperFoilBurnisher(HashMap<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException{
        WindowPatternCard windowPatternCard = (WindowPatternCard) params.get(ToolcardContent.WindowPattern);

        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);

        WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);
        WindowCell end = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);

        if(start.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = start.getAssignedDice();
        try {
            start.removeDice();
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, false, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            start.setAssignedDice(d1);
            throw e;
        }
    }

    private  void handleLathekin(HashMap<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {
        WindowPatternCard windowPatternCard = (WindowPatternCard) params.get(ToolcardContent.WindowPattern);

        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooStart2 = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);
        int[] cooEnd2 = (int[]) params.get(ToolcardContent.WindowCellEnd);

        WindowCell start1 = windowPatternCard.getCell(cooStart[0], cooStart[1]);
        WindowCell start2 = windowPatternCard.getCell(cooStart[0], cooStart[1]);
        WindowCell end1 = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);
        WindowCell end2 = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);

        if(start1.isEmpty() || start2.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end1.isEmpty() || !end2.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = start1.getAssignedDice();
        Dice d2 = start2.getAssignedDice();
        try {
            start1.removeDice();
            start2.removeDice();
            windowPatternCard.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, false);
            windowPatternCard.insertDice(d2, end2.getRow(), end2.getColumn(), true, true, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }
    }



    protected void activateToolcard(VirtualView observable, ToolCardMessage toolCardMessage){

        Player player = observable.getClient();

        try {
            player.getActivePatternCard().useToken();
        } catch (GameException e){
            this.onFailure(observable, e.getMessage());
            return;
        }

        ToolCardsName tcn = toolCardMessage.getToolCardName();
        switch (tcn) {

            case GrozingPliers:
                try {
                    handleGrozingPliers(toolCardMessage.getParameters());
                } catch (ToolCardException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;

            case EnglomiseBrush:
                try {
                    handleEnglomiseBrush(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;

            case CopperFoilBurnisher:
                try {
                    handleCopperFoilBurnisher(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;

            case Lathekin:
                try {
                    handleLathekin(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;

            default: break;
        }
    }
}
