package it.polimi.se2018.controller;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.exception.ToolCardException;
import it.polimi.se2018.message.ControllerCallbackMessage;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.Security;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class implements the controller for the 12 Tool Cards
 */

public class ToolCardController {

    private Game gameAssociated;
    private ToolCard lastToolCardActivated = null;
    protected AtomicBoolean activable = new AtomicBoolean();

    protected ToolCardController(Game gameAssociated){
        this.gameAssociated = gameAssociated;
        this.activable.set(true);
    }

    private ToolCard retrieveToolCardFromName(ToolCardsName tcn){
        for (ToolCard tc : gameAssociated.getToolCards())
            if (tc.getToolCardName().equals(tcn)) {
                lastToolCardActivated = tc;
                return tc;
            }
        return null;
    }

    private void onFailure(VirtualView vv, String errorMessage){
        ControllerCallbackMessage ccm = new ControllerCallbackMessage(CallbackMessageSubject.ToolcardNack , errorMessage, LoggerPriority.NOTIFICATION);
        ccm.setStringMessage(errorMessage);
        vv.controllerCallback(ccm);
    }

    private void onSuccess(VirtualView vv, ToolCardsName name) throws GameException {
        ToolCard toolCard = retrieveToolCardFromName(name);
        Player player = vv.getClient();
        player.getActivePatternCard().useToken( (toolCard.isUsed()) ? 2 : 1 );
        toolCard.setUsed(true);
        activable.set(false);
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
        Die tableDie = this.gameAssociated.getDiceOnTable().get(tableDieIndex);
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
     * This method handles the effects of the following described tool cards:
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

        if (name.equals(ToolCardsName.CorkBackedStraightedge)) {
            Die draftedDie = this.gameAssociated.getDiceOnTable().get((int)params.get(ToolcardContent.DraftedDie));
            windowPatternCard.insertDice(draftedDie, end.getRow(), end.getColumn(), true, true, false);
            return;
        }


        int[] cooStart = (int[]) params.get(ToolcardContent.WindowCellStart);
        WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);

        if (start.isEmpty())
            throw new ToolCardException("EmptyWindowCell");

        Die d1 = start.getAssignedDie();


        //in case this method is called by TapWheel, it's needed to check another condition
        if (name.equals(ToolCardsName.TapWheel)) {
            RoundTrack rt = this.gameAssociated.getRoundTrack();
            DiceColor dieColor = d1.getDiceColor();
            if (!(rt.getColorSet().contains(dieColor)))
                throw new ToolCardException("Roundtrack does not contain a die with the same color");
            }


        if (name.equals(ToolCardsName.CopperFoilBurnisher))
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, false, true);
        else if(name.equals(ToolCardsName.EnglomiseBrush))
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), false, true, true);
        else if (name.equals(ToolCardsName.Lathekin) || name.equals(ToolCardsName.TapWheel) )
            windowPatternCard.insertDice(d1, end.getRow(), end.getColumn(), true, true, true);
        windowPatternCard.decreasePlacedDice();
        start.removeDice();
    }

    /**
     * Tool Card #4 "Lathekin": Move exactly two dice, obeying all placement restrictions.
     * The method which handles this tool card recall handleMovingDiceToolcard twice and include an error handling resetter.
     */
    private  void handleDoubleMovingDiceToolcard(ToolCardsName name, Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();

        Map<ToolcardContent, Object> htc1= new HashMap<>();
        htc1.put(ToolcardContent.RunBy, params.get(ToolcardContent.RunBy));
        htc1.put(ToolcardContent.WindowCellStart, params.get(ToolcardContent.firstWindowCellStart));
        htc1.put(ToolcardContent.WindowCellEnd, params.get(ToolcardContent.firstWindowCellEnd));

        Map<ToolcardContent, Object> htc2 = null;
        //define a second hashmap toolcard content only if you are dealing with Lathekin or with TapWheel with double movement.
        if ( (name.equals(ToolCardsName.Lathekin)) || ( name.equals(ToolCardsName.TapWheel) && (int)params.get(ToolcardContent.Amount) == 2) ) {
            htc2 = new HashMap<>();

            //check if the dice have the same color
            if (name.equals(ToolCardsName.TapWheel)) {
                int cooStart1[] = (int[]) params.get(ToolcardContent.firstWindowCellStart);
                WindowCell windowCellStart1 = windowPatternCard.getCell(cooStart1[0], cooStart1[1]);
                int cooStart2[] = (int[]) params.get(ToolcardContent.secondWindowCellStart);
                WindowCell windowCellStart2 = windowPatternCard.getCell(cooStart2[0], cooStart2[1]);
                Die d1 = windowCellStart1.getAssignedDie();
                Die d2 = windowCellStart2.getAssignedDie();

                if (d1 != null && d2 != null && !d1.getColor().equals(d2.getColor()))
                    throw new ToolCardException("Die have the same color.");
            }

            htc2.put(ToolcardContent.RunBy, params.get(ToolcardContent.RunBy));
            htc2.put(ToolcardContent.WindowCellStart, params.get(ToolcardContent.secondWindowCellStart));
            htc2.put(ToolcardContent.WindowCellEnd, params.get(ToolcardContent.secondWindowCellEnd));
        }


        handleMovingDiceToolcard(name, htc1);

        try {
            if (htc2 != null) handleMovingDiceToolcard(name, htc2);

            // in case an exception is raised during the placement of the second die, the starting situation has to be restored.
        } catch (ToolCardException | NotEmptyWindowCellException e){
            int[] cooEnd = (int[]) htc1.get(ToolcardContent.WindowCellEnd);
            WindowCell end = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);
            Die d = end.getAssignedDie();
            end.removeDice();
            int[] cooStart = (int[]) htc1.get(ToolcardContent.WindowCellStart);
            WindowCell start = windowPatternCard.getCell(cooStart[0], cooStart[1]);
            windowPatternCard.insertDice(d, start.getRow(), start.getColumn(), false, false, false );
            throw e;
        }
    }

    /**
     * Tool Card #5 "Lens Cutter": After drafted swap the drafted die with a die from the round track.
     */
    private void handleLensCutter(Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        int[] rtDieCoordinates = (int[]) params.get(ToolcardContent.RoundTrackDie);
        int rtTurn = rtDieCoordinates[0];
        int rtDieIndex = rtDieCoordinates[1];

        Die rtDie = null;
        Die draftedDie = null;
        try {
            rtDie = this.gameAssociated.getRoundTrack().getTrack().get(rtTurn).get(rtDieIndex);
            draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new ToolCardException("Wrong coordinates");
        }

        this.gameAssociated.getDiceOnTable().set(draftedDieIndex, rtDie);
        this.gameAssociated.getRoundTrack().getTrack().get(rtTurn).set(rtDieIndex, draftedDie);
    }

    /**
     * Tool Card #6 "Flux Brush": After drafting re roll the drafted die. If it cannot be placed,
     * return it to the drafted pool.
     */
    private void handleFluxBrush(Map<ToolcardContent, Object> params) throws NotValidInsertion, NotEmptyWindowCellException{

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();
        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Die draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);
        draftedDie.setNumber((int)params.get(ToolcardContent.RolledNumber));
        int[] cooEnd= (int[])params.get(ToolcardContent.WindowCellEnd);


        if (params.get(ToolcardContent.WindowCellEnd) == null) {
            return;
        } else {
            windowPatternCard.insertDice(draftedDie, cooEnd[0], cooEnd[1], true, true, true);
            windowPatternCard.getPlayer().getLastGameJoined().getDiceOnTable().remove(draftedDie);
        }
    }


    /**
     * Tool Card #7 "Glazing Hammer": Re roll all dice in the drafted pool.
     * This may only used on your second turn before drafting.
     */
    private void handleGlazingHammer(Map<ToolcardContent, Object> params) throws ToolCardException{

        int turn = gameAssociated.getActualTurn() + 1;

        /*if (gameController.getActiveRoundHandler().movableDice == 0){
            throw new ToolCardException("Already drafted a die");
        }*/

        if (turn - this.gameAssociated.getPlayersOrder().size() <= 0 ) {
            throw new ToolCardException("Not Second Turn");
        }

        for(Die die: this.gameAssociated.getDiceOnTable()){
            die.rollDice();
        }
    }


    /**
     * Tool Card #5: After your first turn, immediately draft a die. Skip your next turn this round.
     */
    private void handleRunningPliers(Map<ToolcardContent, Object> params) throws ToolCardException {
        Player player = Security.getUser((String)params.get(ToolcardContent.RunBy));
        if (player.hasToSkipNextTurn()) {
            throw new ToolCardException("Toolcard Already Activated");
        }
        this.gameAssociated.getAssociatedGameController().getActiveRoundHandler().movableDice++;
        player.setSkipNextTurn(true);
    }

    /**
     * Tool Card #10 "Grinding Stone": After drafting flip the die to its opposite side.
     */
    private void handleGrindingStone(Map<ToolcardContent, Object> params) {

        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Die draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        draftedDie.setNumber(7 - draftedDie.getNumber());
    }

    /**
     * Tool Card #11 "Flux Remover": After drafting return the die to the dice bag and pull 1 die from the bag.
     * Choose a value and place the new die, obeying all placement restriction or return to the dice bag.
     */
    private void handleFluxRemover(Map<ToolcardContent, Object> params) throws ToolCardException, NotEmptyWindowCellException {

        WindowPatternCard windowPatternCard = Security.getUser((String)params.get(ToolcardContent.RunBy)).getActivePatternCard();
        int draftedDieIndex = (int) params.get(ToolcardContent.DraftedDie);
        Die draftedDie = this.gameAssociated.getDiceOnTable().get(draftedDieIndex);

        Die bagDie = this.gameAssociated.getDieForSwitch();
        int number = (int) params.get(ToolcardContent.Number);
        int[] cooEnd = (int[]) params.get(ToolcardContent.WindowCellEnd);
        WindowCell wc = windowPatternCard.getCell(cooEnd[0], cooEnd[1]);

        draftedDie.setLocation(DiceLocation.BAG);
        windowPatternCard.insertDice(bagDie, wc.getRow(), wc.getColumn(), true, true, true);

        if (number > 0 &&  number < 7) {
            bagDie.setNumber(number);
        } else throw new ToolCardException("InvalidValue");

        this.gameAssociated.getDiceBag().remove(bagDie);
        this.gameAssociated.setDieForSwitch();
        this.gameAssociated.getDiceOnTable().remove(draftedDie);
    }

    protected void activateToolcard(VirtualView observable, ToolCardMessage toolCardMessage){

        Player player = observable.getClient();
        ToolCardsName tcn = toolCardMessage.getToolCardName();

        if (!activable.get()){
            ((View)observable).controllerCallback(new ControllerCallbackMessage(CallbackMessageSubject.ToolcardNack , "Toolcard already activated.", LoggerPriority.NOTIFICATION));
            return;
        }

        if (!gameAssociated.getActivePlayer().equals(player)){
            ((View)observable).controllerCallback(new ControllerCallbackMessage(CallbackMessageSubject.ToolcardNack , "Not your turn", LoggerPriority.NOTIFICATION));
            return;
        }

        int nOfTokens = 0;
        if (!Server.getInstance().isTestMode()) {
            if (retrieveToolCardFromName(tcn).isUsed()) nOfTokens = 2;
            else nOfTokens = 1;
        }

        if (player.getActivePatternCard().getNumberOfFavorTokens() < nOfTokens) {
            this.onFailure(observable, "Not enough tokens");
            return;
        }
        /*
        if (gameController.getActiveRoundHandler().toolcardActivated){
            this.onFailure(observable, "ToolCard already activated");
            return;
        }*/

        switch (tcn) {

            case GrozingPliers:
                try {
                    handleGrozingPliers(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | GameException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case EnglomiseBrush:
                try {
                    handleMovingDiceToolcard(tcn ,toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case CopperFoilBurnisher:
                try {
                    handleMovingDiceToolcard(tcn,toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case Lathekin:
                try {
                    handleDoubleMovingDiceToolcard(ToolCardsName.Lathekin,toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }

                break;

            case LensCutter:
                try {
                    handleLensCutter(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case GlazingHammer:
                try {
                    handleGlazingHammer(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | GameException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case RunningPliers:
                try {
                    handleRunningPliers(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case CorkBackedStraightedge:
                try {
                    handleMovingDiceToolcard(tcn,toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case GrindingStone:
                try {
                    handleGrindingStone(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case FluxRemover:
                try {
                    handleFluxRemover(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }
                break;

            case TapWheel:
                try {
                    handleDoubleMovingDiceToolcard( ToolCardsName.TapWheel ,toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (ToolCardException | NotEmptyWindowCellException | GameException e){
                    onFailure(observable, e.getMessage());
                    return;
                }

                break;

            case FluxBrush:
                try {
                    this.handleFluxBrush(toolCardMessage.getParameters());
                    this.onSuccess(observable, tcn);
                } catch (GameException | NotValidInsertion | NotEmptyWindowCellException e) {
                    onFailure(observable, e.getMessage());
                    return;
                }


                break;

            default: break;
        }
    }
}
