package it.polimi.se2018.controller;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.view.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer{
    private static ServerController instance = null;
    private Server server = Server.getInstance();
    private ArrayList<GameController> gameControllers;

    private Player getPlayerFromNick(String nickname){
        for (Player p : server.getOnlinePlayers()){
            if (p.getNickname().equals(nickname))
                return p;
        }
        return null;
    }

    public static ServerController getInstance(){
        if(instance == null){
            instance = new ServerController();
        }
        return instance;
    }

    private void resetGame(){
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

    /**
     * Connect player to server and to first avaiable game
     * @param player
     */
    private synchronized void connectPlayer (Player player, Observer o) {
        server.addPlayerToOnlinePlayers(player); //add player to the list of online players
        player.setOnline(true); //set player status to online
        try {
            if (this.server.getCurrentGame() == null) {
                this.server.setCurrentGame(new Game());
            }
            this.server.getCurrentGame().addPlayer(player); // try to add player to the settupping game
            this.server.getCurrentGame().addObserver(o); //add View as observer of the game.
            player.setInGame(true); // set player status to true
            player.setLastGameJoined(server.getCurrentGame()); //change last game joined param
            this.server.addPlayer(server.getInGamePlayers(), player); //add him to the list of ingame players
        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player); //in case of game full and not started, put it in waiting players
        } catch (GameException e){ Logger.ERROR(LoggerType.SERVER_SIDE, e.toString());}
    }

    /**
     * Disconnect player to server
     * @param player
     */
    private synchronized void disconnectPlayer (Player player) {
        server.removePlayerFromOnlinePlayers(player);

        player.setOnline(false);
        player.getLastGameJoined().removePlayer(player);

    }

    private void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  )
                this.connectPlayer(message.getRequester(), (Observer)observable);
        }
        else {
            if ( message.getTarget() == null  ) {
                // delete the VirtualView as observer from Server and from all games.
                server.deleteObserver((VirtualView) observable);
                for (Game g : this.server.getActiveGames()) g.deleteObserver((VirtualView)observable);
                this.disconnectPlayer(message.getRequester());
            }
        }
    }

    private void handleRequestMessage(Observable observable, RequestMessage rMsg){
        if (rMsg.getRequest().equals("SubscribePlayer")) {
            this.getPlayerFromNick(((View) observable).getClient().getNickname()).addObserver((Observer) observable);
        }

        else if (rMsg.getRequest().equals("ConnectedPlayers")){
            ((VirtualView) observable).controllerCallback(new GiveMessage("ConnectedPlayers", server.getOnlinePlayers()));
        }
    }

    public void update (Observable observable, Object message){

        Logger.NOTIFICATION(LoggerType.SERVER_SIDE, ":SERVER_CONTROLLER: Receveid -> " + ((Message) message).getMessageType());
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