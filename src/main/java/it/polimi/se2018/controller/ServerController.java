package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.utils.TimerInterface;
import it.polimi.se2018.view.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer, Serializable{
    private static ServerController instance = null;
    private Server server = Server.getInstance();
    private ArrayList<GameController> gameControllers;

    public static ServerController getInstance(){
        if(instance == null){
            instance = new ServerController();
        }
        return instance;
    }

    /**
     * This method handles the operation that happens to the server after a game starts.
     * If there are not any players in the waiting room, it simply set to null the active game.
     * Otherwise it handles the players moving from the waiting room to the active room.
     */
    protected void resetGame(){
        if (this.server.getWaitingPlayers().isEmpty()) this.server.setCurrentGame(null);
        else {
            this.server.setCurrentGame(new Game());

            for (Player p: this.server.getWaitingPlayers()){
                try {
                    this.server.getCurrentGame().addPlayer(p);
                    this.server.removePlayer(this.server.getWaitingPlayers(), p);
                } catch (GameException e){
                    if (e.getMessage().equals("GameIsFull")) break;
                }
            }
        }
    }

    protected  void removeGame(Game game){
        this.server.getActiveGames().remove(game);
    }

    /**
     * This method handles the connection routine for class server. It's always called together with the same method of
     * game controller.
     * @param observable
     * @param message
     */
    private synchronized void connectPlayer (Observable observable, ConnectionMessage message) {
        Player player = message.getRequester();
        server.addPlayerToOnlinePlayers(player); //add player to the list of online players
        player.setOnline(true); //set player status to online

        if (player.getLastGameJoined() != null) {
            player.getLastGameJoined().getAssociatedGameController().update(observable, message);
            return;
        }

        try {
            if (this.server.getCurrentGame() == null) {
                this.server.setCurrentGame(new Game());
            }
            this.server.getCurrentGame().getAssociatedGameController().update(observable, message);

        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player); //in case of game full and not started, put it in waiting players
        }
    }

    /**
     * Disconnect player from server
     * @param player
     */
    protected synchronized void disconnectPlayer (Player player) {
        server.removePlayerFromOnlinePlayers(player);
        server.addPlayer(server.getOfflinePlayers(), player);
        player.setOnline(false);
    }

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

    private synchronized void handleRequestMessage(Observable observable, RequestMessage rMsg){

        if (rMsg.getRequest().equals("ConnectedPlayers")){
            ((VirtualView) observable).controllerCallback(new GiveMessage("ConnectedPlayers", server.getOnlinePlayers()));
        }
    }

    public void update (Observable observable, Object message){

        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":SERVER_CONTROLLER: Received -> " + ((Message) message).getMessageType());
        switch(((Message)message).getMessageType()){

            case "ConnectionMessage":
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