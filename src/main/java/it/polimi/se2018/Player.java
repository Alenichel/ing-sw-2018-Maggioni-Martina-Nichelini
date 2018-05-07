package it.polimi.se2018;

import java.util.Date;

public class Player {

    private String nickname;
    private Date firstSeen;
    private Boolean online;
    private Boolean inGame;
    private Room lastGameJoined;
    private WindowPatternCard activePatternCard;
    private ObjectiveCard privateObjectiveCard;


    public Player(String nickname) {
        this.nickname = nickname;
    }

    public void setOnline(Boolean status){
        this.online = status;
    }

    public Boolean getOnline(){
        return this.online;
    }

    public void assignPatternCard(WindowPatternCard card){
        if (!inGame) throw new IllegalArgumentException();
        else activePatternCard = card;
    }

    public WindowPatternCard getActivePatternCard() {
        return activePatternCard;
    }

    public void assignObjectiveCard(ObjectiveCard card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }

    public ObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }


}
