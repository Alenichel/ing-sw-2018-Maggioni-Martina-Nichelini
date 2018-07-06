package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.WhatToUpdate;

/**
 * Class for update message
 */
public class UpdateMessage extends Message {

    private WhatToUpdate whatToUpdate;

    /**
     * Class constructor
     */
    public UpdateMessage(WhatToUpdate whatToUpdate){
        this.whatToUpdate = whatToUpdate;
        this.messageType = "UpdateMessage";
    }

    /**
     * what to update getter
     * @return what to update
     */
    public WhatToUpdate getWhatToUpdate() {
        return whatToUpdate;
    }

    /**
     * to string method
     */
    @Override
    public String toString(){
        return this.stringMessage;
    }
}