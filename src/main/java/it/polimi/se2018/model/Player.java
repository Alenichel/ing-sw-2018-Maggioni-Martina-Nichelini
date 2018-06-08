package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.*;

public class Player extends Observable implements Serializable{
    private String nickname;
    private Boolean online;
    private Boolean inGame;
    private Game lastGameJoined;
    private int playerNumber;
    private WindowPatternCard activePatternCard;
    private PrivateObjectiveCard privateObjectiveCard;
    private ArrayList<WindowPatternCard> windowPatternCardsPool;
    private int score;
    private HashMap<String, Integer> scores = new HashMap<>();

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
    public void assignObjectiveCard(PrivateObjectiveCard card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }
    public void setWindowPatternCardsPool(List<WindowPatternCard> windowPatternCardsPool) {
        this.windowPatternCardsPool = (ArrayList<WindowPatternCard>) windowPatternCardsPool;
    }
    public void setScore(int score) {
        this.score = score;
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
    public PrivateObjectiveCard getPrivateObjectiveCard() {
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

    public List<WindowPatternCard> getWindowPatternCardsPool() {
        return windowPatternCardsPool;
    }
    public int getScore() {
        return score;
    }
    public HashMap<String, Integer> getScores() {
        return scores;
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
                Objects.equals(online, player.online) &&
                Objects.equals(inGame, player.inGame) &&
                Objects.equals(lastGameJoined, player.lastGameJoined) &&
                Objects.equals(activePatternCard, player.activePatternCard) &&
                Objects.equals(privateObjectiveCard, player.privateObjectiveCard);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nickname, online, inGame, lastGameJoined, playerNumber,  activePatternCard, privateObjectiveCard);
    }
}
