package it.polimi.se2018.controller;

import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.ToolCardsName;
import it.polimi.se2018.utils.ToolcardContent;

import java.util.HashMap;

public class ToolCardController {

    private Game gameAssociated;

    protected ToolCardController(Game gameAssociated){
        this.gameAssociated = gameAssociated;
    }

    private void handleGrozingPliers(HashMap<ToolcardContent, Object> params){
        int tableDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice tableDie = this.gameAssociated.getDiceOnTable().get(tableDieIndex);
        Boolean increase = (boolean) params.get(ToolcardContent.Increase);

        if (increase && tableDie.getNumber() != 6) {
            tableDie.setNumber(tableDie.getNumber() + 1);
        } else if (tableDie.getNumber() != 1) {
            tableDie.setNumber(tableDie.getNumber() - 1);
        }
    }

    protected void activateToolcard(ToolCardMessage toolCardMessage){

        ToolCardsName tcn = toolCardMessage.getToolCardName();

        switch (tcn) {

            case GrozingPliers:
                handleGrozingPliers(toolCardMessage.getParameters());
                break;

            default: break;
        }
    }
}
