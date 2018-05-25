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
     * This method handles the connection routine for class server. It's always called together with the same method of
     * game controller.
     * @param player
     */
    private synchronized void connectPlayer (Player player) {
        server.addPlayerToOnlinePlayers(player); //add player to the list of online players
        player.setOnline(true); //set player status to online
        try {
            if (this.server.getCurrentGame() == null) {
                this.server.setCurrentGame(new Game());
            }

        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player); //in case of game full and not started, put it in waiting players
        }
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

    private synchronized void handleConnectionMessage(Observable observable, ConnectionMessage message){
        if (message.isConnecting()){
            if ( message.getTarget() == null  ) {
                this.connectPlayer(message.getRequester());
                this.server.getCurrentGame().getAssociatedGameController().update(observable, message);
            }
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

    private synchronized void handleRequestMessage(Observable observable, RequestMessage rMsg){
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