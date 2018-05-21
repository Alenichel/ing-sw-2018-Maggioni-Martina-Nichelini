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
    private void connectPlayer (Player player) {
        server.addPlayer(server.getOnlinePlayers(), player);
        player.setOnline(true);
        try {
            this.server.getCurrentGame().addPlayer(player);
            this.server.addPlayer(server.getInGamePlayers(), player);
        }catch (IndexOutOfBoundsException e){
            this.server.addPlayer(server.getWaitingPlayers(), player);
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
                        this.connectPlayer(((ConnectionMessage)message).getRequester());
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