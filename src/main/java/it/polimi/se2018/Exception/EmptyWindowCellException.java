package it.polimi.se2018.Exception;

import it.polimi.se2018.WindowCell;

public class EmptyWindowCellException extends Exception{
    public EmptyWindowCellException(String message){
        super(message);
    }
}
