package it.polimi.se2018;

import it.polimi.se2018.message.GiveMessage;

import java.util.Observable;

abstract class View extends Observable {

    abstract void requestCallback(GiveMessage giveMessage);
}
