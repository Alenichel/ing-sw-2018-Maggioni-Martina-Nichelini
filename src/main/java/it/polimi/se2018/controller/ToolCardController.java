package it.polimi.se2018.controller;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.Security;
import it.polimi.se2018.view.VirtualView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the controller for the 12 Tool Cards
 */

public class ToolCardController {

    private Game gameAssociated;

    protected ToolCardController(Game gameAssociated){
        this.gameAssociated = gameAssociated;
    }

    private void onFailure(VirtualView vv, String errorMessage){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.ToolcardNack , errorMessage, LoggerPriority.NOTIFICATION);
        ccm.setStringMessage("Toolcard NACK.");
        vv.controllerCallback(ccm);
    }

    private void onSuccess(VirtualView vv, ToolCardsName name){
        Player p = vv.getClient();
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "User: " + p.getNickname() + " successfully activate " + name.toString());
        ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.ToolCardAck ,LoggerPriority.NOTIFICATION);
        ccm.setStringMessage("Toolcard ACK.");
        vv.controllerCallback(ccm);
        this.gameAssociated.triggerUpdate();
    }

    /**
     * Tool Card #1 "Grozing Pliers": After drafting increase or decrease the value of the drafted die by 1.
     */
    private void handleGrozingPliers(Map<ToolcardContent, Object> params) throws ToolCardException{
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

    /**
     * This method handles the effects of the followinf described toolcards:
     *
     *  - Tool Card #2 "Englomise Brush": Move any one die in your windows ignoring the color restriction.
     *    You must obey all other placement restrictions.
     *  - Tool Card #3 "Copper Foil Burnisher": Move any one die in your windows ignoring shade restriction.
     *    You must obey all other placement restriction.
     *  - Tool Card #9 "Cork Backed Straightedge": After drafting, place the die in a spot that is not adjacent to another die.
     *    You must obey all other placement restriction.
     */
    private void handleMovingDiceToolcard( ToolCardsName name, Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();

        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);
        WindowCell end = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);

        if (!end.isEmpty())
            throw new ToolCardException("NotEmptyWindowCell");

        /*if (name.equals(ToolCardsName.CorkBackedStraightedge)) {
            Dice draftedDie = this.gameAssociated.getDiceOnTable().get((int)params.get(ToolcardContent.DraftedDie));
            windowPatternCard.insertDice(draftedDie, end.getRow(), end.getColumn(), true, true, false);
            return;
        }*/


        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);

        if (start.isEmpty())
            throw new ToolCardException("EmptyWindowCell");

        Dice d1 = start.getAssignedDice();
        if (name.equals(ToolCardsName.CopperFoilBurnisher))
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, false, true);
        else if(name.equals(ToolCardsName.EnglomiseBrush))
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, true);
        else if (name.equals(ToolCardsName.Lathekin))
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, true, true);
        start.removeDice();
    }

    /**
     * Tool Card #4 "Lathekin": Move exactly two dice, obeying all placement restrictions.
     * The method which handles this toolcard recall handleMovingDiceToolcard twice and include an error handling resetter.
     */
    private  void handleLathekin(Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();

        Map<ToolcardContent, Object> htc1= new HashMap<>();
        Map<ToolcardContent, Object> htc2= new HashMap<>();

        htc1.put(ToolcardContent.RunBy, params.get(ToolcardContent.RunBy));
        htc2.put(ToolcardContent.RunBy, params.get(ToolcardContent.RunBy));
        htc1.put(ToolcardContent.WindowCellStart, params.get(ToolcardContent.firstWindowCellStart));
        htc1.put(ToolcardContent.WindowCellEnd, params.get(ToolcardContent.firstWindowCellEnd));
        htc2.put(ToolcardContent.WindowCellStart, params.get(ToolcardContent.secondWindowCellEnd));
        htc2.put(ToolcardContent.WindowCellEnd, params.get(ToolcardContent.secondWindowCellEnd));

        handleMovingDiceToolcard(ToolCardsName.Lathekin, htc1);
        try {
            handleMovingDiceToolcard(ToolCardsName.Lathekin, htc2);
        } catch (ToolCardException | NotEmptyWindowCellException e){
            int[] cooEnd = (int[]) htc1.get(ToolcardContent.WindowCellEnd);
            WindowCell end = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);
            Dice d = end.getAssignedDice();
            end.removeDice();
            int[] cooStart = (int[]) htc1.get(ToolcardContent.WindowCellStart);
            WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);
            windowPatternCard.insertDice(d, start.getRow(), start.getColumn(), false, false, false );
        }
    }

    /**
     * Tool Card #5 "Lens Cutter": After drafted swap the drafted die with a die from the round track.
     */
    private void handleLensCutter(Map<ToolcardContent, Object> params) throws ToolCardException {

        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);
        int rtDieIndex = (int) params.get(ToolcardContent.RoundTrackDie);
        Dice rtDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        DiceLocation rtLocation = rtDie.getLocation(); //posizione esatta nel round track

        if (!rtDie.getLocation().equals(DiceLocation.ROUNDTRACK)) {
            throw new ToolCardException(":LENS_CUTTER: this die can not be chosen");
        }

        else {rtDie.setLocation(DiceLocation.TABLE);}


        if (!draftedDie.getLocation().equals(DiceLocation.TABLE)) {
            throw new ToolCardException(":LENS_CUTTER: this die can not be chosen");
        }
        draftedDie.setLocation(rtLocation);
    }

    /**
     * Tool Card #6 "Flux Brush": After drafting re roll the drafted die. If it cannot be placed,
     * return it to the drafted pool.
     */
    private void handleFluxBrush(Map<ToolcardContent, Object> params) {

        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        draftedDie.rollDice();
        //se non può essere piazzato: draftedDie.setLocation(DiceLocation.TABLE)
    }

    /**
     * Tool Card #7 "Glazing Hammer": Re roll all dice in the drafted pool.
     * This may only used on your second turn before drafting.
     */
    private void handleGlazingHammer(Map<ToolcardContent, Object> params) throws ToolCardException{

        int turn = gameAssociated.getActualTurn() + 1;

        if (turn - this.gameAssociated.getPlayersOrder().size() < 0 ) {
            throw new ToolCardException("NotSecondTurn");
        }

        for(Dice die: this.gameAssociated.getDiceOnTable()){
            die.rollDice();
        }
    }


    private void handleRunningPliers(Map<ToolcardContent, Object> params) throws ToolCardException {
        Player player = Security.getUser((String)params.get(ToolcardContent.RunBy));
        if (player.hasToSkipNextTurn()) {
            throw new ToolCardException("ToolcardAlreadyActivated");
        }
        this.gameAssociated.getAssociatedGameController().getActiveRoundHandler().movableDice++;
        player.setSkipNextTurn(true);
    }

    /**
     * Tool Card #10 "Grinding Stone": After drafting flip the die to its opposite side.
     */
    private void handleGrindingStone(Map<ToolcardContent, Object> params) {

        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        draftedDie.setNumber(7 - draftedDie.getNumber());
    }

    /**
     * Tool Card #11 "Flux Remover": After drafting return the die to the dice bag and pull 1 die from the bag.
     * Choose a value and place the new die, obeying all placement restriction or return to the dice bag.
     */
    private void handleFluxRemover(Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();
        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Dice draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        Dice bagDie = this.gameAssociated.getDieForSwitch();
        int number = (int) params.get(ToolcardContent.Number);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);
        WindowCell wc = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);

        /*if(!draftedDie.getLocation().equals(DiceLocation.TABLE)) {
            throw new ToolCardException("IneligibleDie");
        }*/



        /*if(!bagDie.getLocation().equals(DiceLocation.BAG)){
            throw new ToolCardException("IneligibleDie");
        }*/

        draftedDie.setLocation(DiceLocation.BAG);
        windowPatternCard.insertDice(bagDie, wc.getRow(), wc.getColumn(), true, true, true);

        if (number > 0 &&  number < 7) {
            bagDie.setNumber(number);
        } else throw new ToolCardException("InvalidValue");

        this.gameAssociated.getDiceBag().remove(bagDie);
        this.gameAssociated.setDieForSwitch();
        this.gameAssociated.getDiceOnTable().remove(draftedDie);
    }

    /**
     * Tool Card #12 "Tap Wheel": Move up to two dice of the same color that match the color of a die in the round track.
     * You must obey all the placement restriction.
     */
    private void handleTapWheel(Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException{

        int number = (int) params.get(ToolcardContent.Number);
        WindowPatternCard wpc = (WindowPatternCard) params.get(ToolcardContent.WindowPattern);
        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooStart2 = (int[]) params.get(ToolcardContent.WindowCellStart);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);
        int[] cooEnd2 = (int[]) params.get(ToolcardContent.WindowCellEnd);
        WindowCell start1 = wpc.getCell(cooStart[0], cooStart[1]);
        WindowCell start2 = wpc.getCell(cooStart[0], cooStart[1]);
        WindowCell end1 = wpc.getCell(cooEnd[0], cooEnd[1]);
        WindowCell end2 = wpc.getCell(cooEnd[0], cooEnd[1]);

        if (number != 1 || number != 2)
            throw new ToolCardException(":TAP_WHEEL: you can move up to 2 dice");

        if(start1.isEmpty() || start2.isEmpty())
            throw new ToolCardException(":TAP_WHEEL: this window cell is empty");

        if(!end1.isEmpty() || !end2.isEmpty())
            throw new ToolCardException(":TAP_WHEEL: this window cell is not empty");

        Dice d1 = start1.getAssignedDice();
        Dice d2 = start2.getAssignedDice();

        //controllare che d1 sia dello stesso colore di un dado del RoundTrack

        if (number == 1) {
            try {
                start1.removeDice();
                wpc.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, true);
            }catch (ToolCardException | NotEmptyWindowCellException e) {
                throw e;
            }

            start1.removeDice();
        }

        //controllare che d1 e d2 siano dello stesso colore di un dado del RoundTrack

        if (number == 2) {
            try {
                start1.removeDice();
                start2.removeDice();
                wpc.insertDice(d1, end1.getRow(), end1.getColumn(), true, true, false);
                wpc.insertDice(d2, end2.getRow(), end2.getColumn(), true, true, false);
            }catch (ToolCardException | NotEmptyWindowCellException e) {
                throw e;
            }

            start1.removeDice();
            start2.removeDice();
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
                this.onSuccess(observable, tcn);
                break;

            case EnglomiseBrush:
                try {
                    handleMovingDiceToolcard(tcn ,toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;

            case CopperFoilBurnisher:
                try {
                    handleMovingDiceToolcard(tcn,toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;

            case Lathekin:
                try {
                    handleLathekin(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;/*

            case LensCutter:
                try {
                    handleLensCutter(toolCardMessage.getParameters());
                } catch (ToolCardException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable);
                break;*/
            /*
            case GlazingHammer:
                try {
                    handleGlazingHammer(toolCardMessage.getParameters());
                } catch (ToolCardException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;

            case RunningPliers:
                try {
                    handleRunningPliers(toolCardMessage.getParameters());
                } catch (ToolCardException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;

            case CorkBackedStraightedge:
                try {
                    handleMovingDiceToolcard(tcn,toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;/*

            case GrindingStone:
                handleGrindingStone(toolCardMessage.getParameters());
                this.onSuccess(observable, tcn);
                break;*/

            case FluxRemover:
                try {
                    handleFluxRemover(toolCardMessage.getParameters());
                } catch (ToolCardException | NotEmptyWindowCellException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                this.onSuccess(observable, tcn);
                break;/*

            case TapWheel:
                try {
                    handleTapWheel(toolCardMessage.getParameters());
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
