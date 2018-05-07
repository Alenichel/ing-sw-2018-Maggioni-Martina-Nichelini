package it.polimi.se2018.Exception;

import it.polimi.se2018.WindowCell;

public class NotEmptyWindowCellException extends Exception{
    public NotEmptyWindowCellException(String message){
        super(message);
    }
}
