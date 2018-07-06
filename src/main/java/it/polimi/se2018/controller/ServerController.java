package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.view.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This Singleton implements Server controller
 */
public class ServerController implements Observer, Serializable{
    private static ServerController instance = null;
    private Server server = Server.getInstance();
    private ArrayList<GameController> gameControllers;

    /**
     * Server controller singleton constructor
     * @return instance
     */
    public static ServerController getInstance(){
        if(instance == null){
            instance = new ServerController();
        }
        return instance;
    }

    /**
     * This method handles the operation that happens to the server after a game starts.
     * If there are not any players in the waiting room, it simply sets the active game to null,
     * otherwise it handles the players moving from the waiting room to the active room.
     */
    protected void resetGame(){
        if (this.server.getWaitingPlayers().isEmpty()) this.server.setCurrentGame(null);
        else {
            this.server.setCurrentGame(new Game());

            for (Player p: this.server.getWaitingPlayers()){
                this.server.removePlayer(this.server.getWaitingPlayers(), p);
                ConnectionMessage cm = new ConnectionMessage(p, true);
                this.connectPlayer(p.getVv(), cm);
            }
        }
    }

    /**
     * This method removes a game from the active games and handles all the associated operations
     * @param game game to be removed
     */
    protected  void removeGame(Game game){

        //action to perform on all the players who joined the game.
        for (Player p: game.getPlayersOrder()) {
            p.setLastGameJoined(null);
        }

        //action to perform only to players that are still connected to the game.
        for (Player p: game.getPlayers()){
            p.setInGame(false);
            p.getVv().deleteObservers();
            server.removePlayer(server.getInGamePlayers(), p);
            server.addObserver(p.getVv());
            p.getVv().addObserver(ServerController.getInstance());
        }
        this.server.getActiveGames().remove(game);
    }

    /**
     * This method handles the connection routine for class server. It's always called together with the same method of
     * game controller.
     * @param observable Observable
     * @param message ConnectionMessage
     */
    private synchronized void connectPlayer (Observable observable, ConnectionMessage message) {

        Player player = ((VirtualView)observable).getClient();
        if (!server.getOnlinePlayers().contains(player)) server.addPlayerToOnlinePlayers(player); //add player to the list of online players
        player.setOnline(true); //set player status to online

        if (player.getLastGameJoined() != null) { //case the player was previously connected to a game
            player.getLastGameJoined().getAssociatedGameController().update(observable, message);
            return;
        }

        try {
            if (this.server.getCurrentGame() == null) { //case the game is not created yet
                this.server.setCurrentGame(new Game());
            }
            this.server.getCurrentGame().getAssociatedGameController().update(observable, message);

        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player); //in case of game full and not started, put it in waiting players
        }
    }

    /**
     * This method disconnects a player from the server
     * @param player to be disconnected
     */
    protected synchronized void disconnectPlayer (Player player) {
        server.removePlayer(server.getInGamePlayers(), player);
        server.removePlayerFromOnlinePlayers(player);
        player.setVv(null);
        player.setOnline(false);
    }

    /**
     * This method handles the connection message
     * @param observable Observable
     * @param message ConnectionMessage
     */
    private synchronized void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  ) {
                this.connectPlayer(observable, message);
            }
        }

        else {
            if ( message.getTarget() == null  ) {
                this.disconnectPlayer(message.getRequester());
            }
        }
    }

    /**
     * This method handles the request message
     * @param observable Observable
     * @param rMsg RequestMessage
     */
    private synchronized void handleRequestMessage(Observable observable, RequestMessage rMsg){

        if (rMsg.getRequest().equals("ConnectedPlayers")){
            ((VirtualView) observable).controllerCallback(new GiveMessage("ConnectedPlayers", server.getOnlinePlayers()));
        }
    }

    public synchronized void update (Observable observable, Object message){

        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, ":SERVER_CONTROLLER: Received -> " + ((Message) message).getMessageType());
        switch(((Message)message).getMessageType()){

            case "ConnectionMessage":
               handleConnectionMessage(observable, (ConnectionMessage)message);
               break;

            case "ReConnectionMessage":
                handleConnectionMessage(observable, (ConnectionMessage)message);
                break;

            case "RequestMessage":
                handleRequestMessage(observable, (RequestMessage)message);
                break;

            default:
                break;
        }
    }


}