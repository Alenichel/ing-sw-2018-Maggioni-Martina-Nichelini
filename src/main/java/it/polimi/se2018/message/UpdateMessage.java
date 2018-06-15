package it.polimi.se2018.message;

import it.polimi.se2018.enumeration.WhatToUpdate;

public class UpdateMessage extends Message {

    private WhatToUpdate whatToUpdate;


    public UpdateMessage(WhatToUpdate whatToUpdate){
        this.whatToUpdate = whatToUpdate;
        this.messageType = "UpdateMessage";
    }


    public WhatToUpdate getWhatToUpdate() {
        return whatToUpdate;
    }

    @Override
    public String toString(){
        return this.stringMessage;
    }
}
