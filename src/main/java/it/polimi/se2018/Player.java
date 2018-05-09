package it.polimi.se2018;

import java.util.ArrayList;
import java.util.Date;

public class Player {

    private String nickname;
    private Date firstSeen;
    private Boolean online;
    private Boolean inGame;
    private Room lastGameJoined;
    private WindowPatternCard activePatternCard;
    private ArrayList<WindowPatternCard> patternCardPool;
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
    public ObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }
    public WindowPatternCard getActivePatternCard() {
        return activePatternCard;
    }
    public String getNickname() {
        return nickname;
    }

    public void setPatternCardPool(ArrayList<WindowPatternCard> patternCardPool) {
        this.patternCardPool = patternCardPool;
    }

    public ArrayList<WindowPatternCard> getPatternCardPool() {
        return patternCardPool;
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
