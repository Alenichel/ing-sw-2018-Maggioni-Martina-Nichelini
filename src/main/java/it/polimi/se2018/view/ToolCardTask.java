package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ToolCardTask extends Task<Void> {

    private final ToolCard toolCard;
    private final GuiView guiView;
    private final GameWindowController gwc;
    private final Semaphore toolcardSemaphore;

    public ToolCardTask (ToolCard toolcard, GuiView guiView, Semaphore toolcardSemaphore) {
        this.toolCard = toolcard;
        this.guiView = guiView;
        this.gwc = guiView.gameWindowController;
        this.toolcardSemaphore = toolcardSemaphore;
    }

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

    @Override
    protected Void call() throws Exception {
        ToolcardContent[] content = toolCard.getContent();
        Map<ToolcardContent, Object> htc = new HashMap<>();

        for (ToolcardContent tc : content){
            if (tc.equals(ToolcardContent.RunBy)) {
                htc.put(tc, guiView.client.getNickname());
                continue;
            }
            else if (tc.equals(ToolcardContent.BagDie)) continue;
             this.handleGuiSetup(tc);
            if (! toolcardSemaphore.tryAcquire(Server.getInstance().getDefaultMoveTimer(), TimeUnit.SECONDS)) {
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.WARNING, "TIMEOUT DONE: Task stopped");
                return null;
            }
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
