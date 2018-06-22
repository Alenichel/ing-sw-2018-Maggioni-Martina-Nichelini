package it.polimi.se2018.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;
import java.util.*;

/**
 * This class implements the player with his state, the cards he haves and his score
 */
public class Player extends Observable implements Serializable{
    private String nickname;
    private Boolean online;
    private Boolean inGame;
    private Boolean skipNextTurn = false;
    private Game lastGameJoined;
    private int playerNumber;
    private WindowPatternCard activePatternCard;
    private PrivateObjectiveCard privateObjectiveCard;
    private List<WindowPatternCard> windowPatternCardsPool;
    private int score;
    private HashMap<String, Integer> scores = new HashMap<>();

    /**
     * Player constructor
     * @param nickname: player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.inGame = false;
    }

    /**
     * This method sets a player in a game if "inGame" is true
     */
    public void setInGame(Boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * This method sets a player online if "status" is true
     */
    public void setOnline(Boolean status){
        this.online = status;
    }

    /**
     * This method makes the player skip a turn if "skipTurn" is true
     * @param skipTurn
     */
    public void setSkipNextTurn(Boolean skipTurn) {
        this.skipNextTurn = skipTurn;
    }

    /**
     * Active window pattern cards setter
     * @param windowPatternCard: player's chosen pattern card
     */
    public void setActivePatternCard(WindowPatternCard windowPatternCard){
        this.activePatternCard = windowPatternCard;
    }

    /**
     * Player number setter
     */
    public void setPlayerNumber(int number){
        this.playerNumber = number;
    }

    /**
     * Last game joined setter
     * @param game: last game joined
     */
    public void setLastGameJoined(Game game){
        this.lastGameJoined = game;
    }

    /**
     * This method assigns a window pattern card
     * @param card: window pattern card assigned
     */
    public void assignPatternCard(WindowPatternCard card){
        if (!inGame) throw new IllegalArgumentException();
        else activePatternCard = card;
    }

    /**
     * This method assigns a private objective card
     * @param card: private objective card assigned
     */
    public void assignObjectiveCard(PrivateObjectiveCard card){
        if (!inGame ) throw  new IllegalArgumentException();
        else privateObjectiveCard = card;
    }

    /**
     * Window pattern cards pool setter
     * @param windowPatternCardsPool: list of window pattern cards
     */
    public void setWindowPatternCardsPool(List<WindowPatternCard> windowPatternCardsPool) {
        this.windowPatternCardsPool = (ArrayList<WindowPatternCard>) windowPatternCardsPool;
    }

    /**
     * Score setter
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * In game getter
     * @return true if the player is in game, false otherwise
     */
    public Boolean getInGame() {
        return inGame;
    }

    /**
     * Online getter
     * @return true if the player is online, false otherwise
     */
    public Boolean getOnline(){
        return this.online;
    }

    /**
     * Boolean has to skip next turn
     * @return true if the player has to skip next turn, false otherwise
     */
    public Boolean hasToSkipNextTurn () {
        return this.skipNextTurn;
    }

    /**
     * Last game joined getter
     * @return las game joined
     */
    public Game getLastGameJoined() {
        return lastGameJoined;
    }

    /**
     * Private objective card getter
     * @return private objective card
     */
    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    /**
     * Active window pattern card getter
     * @return active window pattern card
     */
    public WindowPatternCard getActivePatternCard() {
        return activePatternCard;
    }

    /**
     * Nickname getter
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Player number getter
     * @return player number
     */
    public int getPlayerNumber(){
        return this.playerNumber;
    }

    /**
     * Window pattern cards pool getter
     * @return window pattern cards pool
     */
    public List<WindowPatternCard> getWindowPatternCardsPool() {
        return windowPatternCardsPool;
    }

    /**
     * Score getter
     * @ score
     */
    public int getScore() {
        return score;
    }

    /**
     * Scores getter
     * @return scores
     */
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
