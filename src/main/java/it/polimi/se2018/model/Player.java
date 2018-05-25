package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.*;

public class Player extends Observable implements Serializable{

    private List<Observer> observers = new ArrayList<>();

    private String nickname;
    private Date firstSeen;
    private Boolean online;
    private Boolean inGame;
    private Game lastGameJoined;
    private int playerNumber;
    private WindowPatternCard activePatternCard;
    private String privateObjectiveCard;
    private int numberOfFavorTokens;

    public Player(String nickname) {
        this.nickname = nickname;
        this.inGame = false;
    }

    public void setInGame(Boolean inGame) {
        this.inGame = inGame;
    }
    public void setOnline(Boolean status){
        this.online = status;
    }
    public void setActivePatternCard(WindowPatternCard windowPatternCard){
        this.activePatternCard = windowPatternCard;
        this.numberOfFavorTokens = windowPatternCard.getNumberOfFavorTokens();
    }
    public void setPlayerNumber(int number){
        this.playerNumber = number;
    }
    public void setLastGameJoined(Game game){
        this.lastGameJoined = game;
    }
    public void assignPatternCard(WindowPatternCard card){
        if (!inGame) throw new IllegalArgumentException();
        else activePatternCard = card;
    }
    public void assignObjectiveCard(String card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }
    public void setNumberOfFavorTokens(int numberOfFavorTokens) {
        this.numberOfFavorTokens = numberOfFavorTokens;
    }

    public Boolean getInGame() {
        return inGame;
    }
    public Boolean getOnline(){
        return this.online;
    }
    public Game getLastGameJoined() {
        return lastGameJoined;
    }
    public String getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }
    public WindowPatternCard getActivePatternCard() {
        return activePatternCard;
    }
    public String getNickname() {
        return nickname;
    }
    public int getPlayerNumber(){
        return this.playerNumber;
    }
    public int getNumberOfFavorTokens() {
        return numberOfFavorTokens;
    }

    @Override
    public String toString(){
        return this.nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerNumber == player.playerNumber &&
                Objects.equals(nickname, player.nickname) &&
                Objects.equals(firstSeen, player.firstSeen) &&
                Objects.equals(online, player.online) &&
                Objects.equals(inGame, player.inGame) &&
                Objects.equals(lastGameJoined, player.lastGameJoined) &&
                Objects.equals(activePatternCard, player.activePatternCard) &&
                Objects.equals(privateObjectiveCard, player.privateObjectiveCard);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nickname, firstSeen, online, inGame, lastGameJoined, playerNumber,  activePatternCard, privateObjectiveCard);
    }
}
