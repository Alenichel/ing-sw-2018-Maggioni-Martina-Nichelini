package it.polimi.se2018.model;

import java.util.Date;
import java.util.Objects;

public class Player {

    private String nickname;
    private Date firstSeen;
    private Boolean online;
    private Boolean inRoom;
    private Room room;
    private Boolean inGame;
    private Room lastGameJoined;
    private int playerNumber;
    private WindowPatternCard activePatternCard;
    private ObjectiveCard privateObjectiveCard;

    public Player(String nickname) {
        this.nickname = nickname;
        this.inRoom = false;
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
    public void setLastGameJoined(Room game){
        this.lastGameJoined = game;
    }
    public void assignPatternCard(WindowPatternCard card){
        if (!inGame) throw new IllegalArgumentException();
        else activePatternCard = card;
    }
    public void assignObjectiveCard(ObjectiveCard card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }
    public Boolean getInRoom() {
        return this.inRoom;
    }
    public Boolean getInGame() {
        return inGame;
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
    public int getPlayerNumber(){
        return this.playerNumber;
    }
    public Room getLastGameJoined() {
        return this.lastGameJoined;
    }
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room, Boolean isConnecting) {
        this.inRoom = isConnecting;
        if (isConnecting) this.room = room;
        else this.room = null;
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
