package it.polimi.se2018.message;

import it.polimi.se2018.utils.ToolCardsName;
import it.polimi.se2018.utils.ToolcardContent;

import java.util.ArrayList;
import java.util.HashMap;

public class ToolCardMessage {

    private ToolCardsName toolCardName;
    private HashMap<ToolcardContent, Object> parameters;

    public ToolCardMessage(ToolCardsName toolCardName, HashMap<ToolcardContent, Object> parameters){
        this.toolCardName = toolCardName;
        this.parameters = parameters;

    }
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }

    public HashMap<ToolcardContent, Object> getParameters() {
        return parameters;
    }
}
