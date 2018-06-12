package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.message.CallbackMessageSubject;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
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
                } catch (ToolCardException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;

            default: break;


        }
    }
}
