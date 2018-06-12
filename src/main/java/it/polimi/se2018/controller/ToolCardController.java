package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.message.CallbackMessageSubject;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.ToolCardsName;
import it.polimi.se2018.utils.ToolcardContent;
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
        WindowCell start = (WindowCell) params.get(ToolcardContent.WindowCellStart);
        WindowCell end = (WindowCell) params.get(ToolcardContent.WindowCellEnd);

        if(start.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = start.getAssignedDice();
        try {
            start.removeDice();
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            start.setAssignedDice(d1);
            throw e;
        }

        start.setAssignedDice(null);
    }

    /*private  void handleCopperFoilBurnisher(HashMap<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException{
        WindowPatternCard windowPatternCard = (WindowPatternCard) params.get(ToolcardContent.WindowPattern);
        WindowCell start = (WindowCell) params.get(ToolcardContent.WindowCellStart);
        WindowCell end = (WindowCell) params.get(ToolcardContent.WindowCellEnd);
        if(start.isEmpty())
            throw new ToolCardException("empty window cell");

        if(!end.isEmpty())
            throw new ToolCardException("not empty window cell");


        Dice d1 = start.getAssignedDice();
        try {
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, false, false);
        }catch (ToolCardException | NotEmptyWindowCellException e) {
            throw e;
        }

        start.removeDice();
    }*/

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

            /*case CopperFoilBurnisher:
                try {
                    handleCopperFoilBurnisher(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;*/

            default: break;




        }
    }
}
