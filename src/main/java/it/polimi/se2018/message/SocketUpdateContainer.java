package it.polimi.se2018.message;

import java.util.Observable;

public class SocketUpdateContainer extends Message {

    private Observable observable;
    private Object object;

    public SocketUpdateContainer(Observable o, Object object){
        this.observable = o;
        this.object = object;
    }

    public Observable getObservable() {
        return observable;
    }

    public Object getObject() {
        return object;
    }
}
