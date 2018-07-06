package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.ToolCardsName;
import it.polimi.se2018.enumeration.ToolcardContent;
import java.util.Map;

/**
 * Class for tool card message
 */
public class ToolCardMessage extends Message{

    private ToolCardsName toolCardName;
    private Map<ToolcardContent, Object> parameters;

    /**
     * Class constructor
     */
    public ToolCardMessage(ToolCardsName toolCardName, Map<ToolcardContent, Object> parameters){
        this.toolCardName = toolCardName;
        this.parameters = parameters;
        this.messageType="ToolcardMessage";

    }

    /**
     * Tool card name getter
     * @return tool card name
     */
    public ToolCardsName getToolCardName() {
        return toolCardName;
    }

    /**
     * Parameters getter
     * @return parameters
     */
    public Map<ToolcardContent, Object> getParameters() {
        return parameters;
    }
}