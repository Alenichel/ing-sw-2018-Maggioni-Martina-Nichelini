package it.polimi.se2018;

import java.util.Date;

public class Player {

    private String nickname;
    private Date firstSeen;
    private Boolean online;
    private Boolean inGame;
    private GameHandler lastGameJoined;
    private WindowPatternCard activePatternCard;
    private ObjectiveCard privateObjectiveCard;

    public Player() {
    }

    public void requestGameConnection(GameHandler game){

    }

    public void requestGameDisconnection(GameHandler game){

    }

    public WindowPatternCard getActivePatternCard() {
        return activePatternCard;
    }

    public ObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public void assignPatternCard(WindowPatternCard card){
        if (!inGame) throw new IllegalArgumentException();
        else activePatternCard = card;
    }

    public void assignObjectiveCard(ObjectiveCard card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }


}
