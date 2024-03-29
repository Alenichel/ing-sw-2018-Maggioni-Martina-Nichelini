package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Tool card task class
 */
public class ToolCardTask extends Task<Void> {

    private final ToolCard toolCard;
    private final GuiView guiView;
    private final GameWindowController gwc;
    private final Semaphore toolcardSemaphore;

    /**
     * Class constructor
     */
    public ToolCardTask (ToolCard toolcard, GuiView guiView, Semaphore toolcardSemaphore) {
        this.toolCard = toolcard;
        this.guiView = guiView;
        this.gwc = guiView.gameWindowController;
        this.toolcardSemaphore = toolcardSemaphore;
    }

    /**
     * This method handles the gui setup
     * @param tc tool card content
     */
    private void handleGuiSetup (ToolcardContent tc) {
        if (tc.equals(ToolcardContent.WindowCellStart) || tc.equals(ToolcardContent.firstWindowCellStart) || tc.equals(ToolcardContent.secondWindowCellStart))
            this.gwc.toolcardWindowEffect(ToolcardContent.WindowCellStart);

        else if (tc.equals(ToolcardContent.WindowCellEnd) || tc.equals(ToolcardContent.firstWindowCellEnd) || tc.equals(ToolcardContent.secondWindowCellEnd))
            this.gwc.toolcardWindowEffect(ToolcardContent.WindowCellEnd);

        else if (tc.equals(ToolcardContent.DraftedDie))
            this.gwc.toolcardDraftPoolEffect();

        else if (tc.equals(ToolcardContent.Increase))
            this.gwc.toolcardIncreaseEffect();

        else if (tc.equals(ToolcardContent.Number))
            this.gwc.toolcardDiceSelection(this.guiView.getClient().getLastGameJoined().getDieForSwitch().getDiceColor());

        else if (tc.equals(ToolcardContent.RoundTrackDie))
            this.gwc.toolcardRoundTrackDie();

        else if(tc.equals(ToolcardContent.Amount)){
            this.gwc.toolCardAmount();
        }
    }

    /**
     * call method
     */
    @Override
    protected Void call() throws Exception {
        int amount = 0;
        ToolcardContent[] content = toolCard.getContent();
        Map<ToolcardContent, Object> htc = new HashMap<>();

        if (content != null)
            for (ToolcardContent tc : content) {
                if (tc.equals(ToolcardContent.RunBy)) {
                    htc.put(tc, guiView.client.getNickname());
                    continue;
                }

                else if (tc.equals(ToolcardContent.BagDie)) continue;

                else if ((tc.equals(ToolcardContent.secondWindowCellStart) || tc.equals(ToolcardContent.secondWindowCellEnd)) && amount == 1) continue;

                else if (tc.equals(ToolcardContent.WindowCellEnd) && toolCard.getToolCardName().equals(ToolCardsName.FluxBrush)){
                    gwc.draftedSelection.setVisible(true);
                    gwc.draftedSelection.setDisable(false);
                    gwc.draftedSelection.setStyle("-fx-border-color:black; -fx-background-color: white;");
                }

                if (tc.equals(ToolcardContent.RolledNumber)) {
                    int dieIndex = (int) htc.get(ToolcardContent.DraftedDie);
                    Die die = guiView.lastGameReceived.getDiceOnTable().get(dieIndex);
                    die.rollDice();
                    int newNumber = die.getNumber();
                    htc.put(tc, newNumber);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gwc.printGameWindow(guiView.lastGameReceived, guiView.getClient(), guiView);
                        }
                    });
                    continue;
                }


                this.handleGuiSetup(tc);

                if (!toolcardSemaphore.tryAcquire(Server.getInstance().getDefaultMoveTimer(), TimeUnit.SECONDS)) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "TIMEOUT DONE: Task stopped");
                    return null;
                }

                if (tc.equals(ToolcardContent.Amount)) amount = (int) guiView.toolCardDragBoard;

                htc.put(tc, guiView.toolCardDragBoard);
        }

        ToolCardMessage tcm = new ToolCardMessage(toolCard.getToolCardName(), htc);
        this.guiView.mySetChanged();
        guiView.notifyObservers(tcm);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gwc.onToolcardEnd();
            }
        });

        return null;
    }
}
