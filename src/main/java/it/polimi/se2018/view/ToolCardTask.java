package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import it.polimi.se2018.message.ToolCardMessage;
import it.polimi.se2018.model.ToolCard;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

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
        if (tc.equals(ToolcardContent.WindowCellStart))
            this.gwc.ToolcardWindowEffect();

        else if (tc.equals(ToolcardContent.DraftedDie))
            this.gwc.ToolcardDraftPoolEffect();
    }

    @Override
    protected Void call() throws Exception {
        ToolcardContent[] content = toolCard.getContent();
        Map<ToolcardContent, Object> htc = new HashMap<>();
        Object toolCardDragBoard = guiView.gameWindowController.toolCardDragBoard;

        for (ToolcardContent tc : content){
            if (tc.equals(ToolcardContent.RunBy)) {
                htc.put(tc, guiView.client);
                continue;
            }
            if (tc.equals(ToolcardContent.Increase)){
                htc.put(tc, true);
                continue;
            }
            this.handleGuiSetup(tc);
            if (this.isCancelled()) break;
            toolcardSemaphore.acquireUninterruptibly();
            htc.put(tc, toolCardDragBoard);
        }

        ToolCardMessage tcm = new ToolCardMessage(toolCard.getToolCardName(), htc);
        this.guiView.mySetChanged();
        guiView.notifyObservers(tcm);

        return null;
    }
}
