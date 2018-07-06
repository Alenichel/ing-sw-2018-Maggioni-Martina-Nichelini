package it.polimi.se2018.message;

import java.util.Observable;

/**
 * Class for scket update container
 */
public class SocketUpdateContainer extends Message {

    private Observable observable;
    private Object object;

    /**
     * Class constructor
     */
    public SocketUpdateContainer(Observable o, Object object){
        this.observable = o;
        this.object = object;
    }

    /**
     * observable getter
     * @return observable
     */
    public Observable getObservable() {
        return observable;
    }

    /**
     * object getter
     * @return object
     */
    public Object getObject() {
        return object;
    }
}