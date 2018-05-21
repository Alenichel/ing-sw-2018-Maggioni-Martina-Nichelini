package it.polimi.se2018.controller;

import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.view.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ServerController implements Observer{
    private static ServerController instance = null;
    private Server server = Server.getInstance();

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

    /**
     * Connect player to server and to first avaiable game
     * @param player
     */
    private void connectPlayer (Player player, Observer o) {
        server.addPlayer(server.getOnlinePlayers(), player); //add player to the list of online players
        player.setOnline(true); //set player status to online
        try {
            this.server.getCurrentGame().addPlayer(player); // try to add player to the settupping game
            this.server.getCurrentGame().addObserver(o);
            player.setInGame(true); // set player status to true
            player.setLastGameJoined(server.getCurrentGame()); //change last game joined param
            this.server.addPlayer(server.getInGamePlayers(), player); //add him to the list of ingame players
        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player); //in case of game full and not started, put it in waiting players
        }
    }


    /**
     * Disconnect player to server
     * @param player
     */
    private void disconnectPlayer (Player player) {
        server.removePlayer(server.getOnlinePlayers(), player);
        player.setOnline(false);
    }


    public void update (Observable observable, Object message){

        System.out.println(((Message)message).getMessageType());
        switch(((Message)message).getMessageType()){

            case "ConnectionMessage":
                if (((ConnectionMessage)message).isConnecting()){
                    if ( ((ConnectionMessage)message).getTarget() == null  )
                        this.connectPlayer(((ConnectionMessage)message).getRequester(), (Observer)observable);
                }
                else {
                    if ( ((ConnectionMessage)message).getTarget() == null  )
                        this.disconnectPlayer(((ConnectionMessage)message).getRequester());
                }
            break;


            case "RequestMessage":
                RequestMessage rMsg = ((RequestMessage)message);

                if (rMsg.getRequest().equals("SubscribePlayer")) {
                    this.getPlayerFromNick(((View) observable).getClient().getNickname()).addObserver((Observer) observable);
                    System.out.println("iscrittoAlPlayer");
                }

                else if (rMsg.getRequest().equals("ConnectedPlayers")){
                    ((VirtualView) observable).controllerCallback(new GiveMessage("ConnectedPlayers", server.getOnlinePlayers()));
                }
            break;

            default:
                break;
        }
    }

}