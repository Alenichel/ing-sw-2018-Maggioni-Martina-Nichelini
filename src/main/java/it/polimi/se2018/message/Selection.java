package it.polimi.se2018.message;

public class Selection {

    private Object chosenItem;

    public Selection(Object item){
        this.chosenItem = item;
    }

    public Object getChosenItem(){
        return chosenItem;
    }
}