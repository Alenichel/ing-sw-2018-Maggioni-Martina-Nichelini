package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import java.util.Map;

public class ToolCardMessage extends Message{

    private ToolCardsName toolCardName;
    private Map<ToolcardContent, Object> parameters;

    public ToolCardMessage(ToolCardsName toolCardName, Map<ToolcardContent, Object> parameters){
        this.toolCardName = toolCardName;
        this.parameters = parameters;
        this.messageType="ToolcardMessage";

    }
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }

    public Map<ToolcardContent, Object> getParameters() {
        return parameters;
    }
}