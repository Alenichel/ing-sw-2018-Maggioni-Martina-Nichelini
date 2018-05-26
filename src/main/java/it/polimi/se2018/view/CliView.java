package it.polimi.se2018.view;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;
import it.polimi.se2018.utils.StringUtils;

import java.util.*;

public class CliView extends View implements Observer {

    private Object lastObjectReceveid;
    private Player player;


    public void setPlayer(Player player) {
        this.player = player;
    }

    private void handleSelectCommands(String command){
        SelectionMessage sm = new SelectionMessage(Integer.valueOf(command)-1, this.player,"PatternCard");
        this.setChanged();
        this.notifyObservers(sm);
    }

    private void handleGetCommands(String command){

        switch(command){

            case "connectedplayers":
                    this.setChanged();
                    this.notifyObservers(new RequestMessage("ConnectedPlayers"));
                    break;

            case "PlayerInGame":
                this.setChanged();
                this.notifyObservers(new RequestMessage("PlayerInGame"));
                break;
            default: break;
        }
    }

    private void handleRequestCommands(String command){

        switch(command){
            //from the game
            case "disconnection":
                if (this.client.getInGame()) {
                    this.setChanged();
                    this.notifyObservers(new ConnectionMessage(this.client, false));
                }
                break;

            default: break;
        }
    }

    private void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, lastObjectReceveid.toString());
    }

    public void controllerCallback(Message callbackMessage){
        this.requestCallback((GiveMessage)callbackMessage);
    }

    public void run() {
        Scanner sinput = new Scanner(System.in);
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, "Cli started..");
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, "*** " + this.player.getNickname() + " ***");

        loop: while (true) {
            String input = sinput.nextLine();
            String[] tokens = input.toLowerCase().split(" ");

            switch (tokens[0]) {

                case "request":
                    try {
                        this.handleRequestCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                    }
                    break;

                case "get":
                    try {
                        this.handleGetCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                        Logger.ERROR(LoggerType.CLIENT_SIDE, "not valid command found");
                    }
                    break;

                case "select":
                    handleSelectCommands(tokens[1]);
                    break;

                case "quit":
                    this.setChanged();
                    this.notifyObservers(new ConnectionMessage(client, false));
                    Logger.log(LoggerType.CLIENT_SIDE,"[*] Goodbye");
                    break loop;

                default:
                    Logger.ERROR(LoggerType.CLIENT_SIDE, "Unrecognized command");
                    break;
            } //end while
        }
    }

    private void onGameStarted(Observable o){
        Game game = ((Game)o);
        ArrayList<Player> players = new ArrayList<>(game.getPlayers());
        int i = 1;
        Logger.log(LoggerType.CLIENT_SIDE, "Select one these cards : ");

        ArrayList<WindowPatternCard> pool = null;
        for(Player p : game.getPlayers()){
            if(p.getNickname().equals(player.getNickname())) {
                pool = p.getWindowPatternCardsPool();
                for (WindowPatternCard w : pool) {
                    Logger.log(LoggerType.CLIENT_SIDE, ((Integer) i).toString() + ") " + w.getName());
                    Logger.log(LoggerType.CLIENT_SIDE, ("Number of favor tokens : " + w.getNumberOfFavorTokens()));
                    Logger.log(LoggerType.CLIENT_SIDE, w.toString());
                    i++;
                }
            }
        }
    }

    public void update(Observable o, Object msg){
        switch(((Message)msg).getMessageType()){
            case "UpdateMessage":
                String wtu = ((UpdateMessage)msg).getWhatToUpdate();
                Logger.NOTIFICATION(LoggerType.CLIENT_SIDE,msg.toString());
                if(wtu.equals("GameStarted")) {
                    onGameStarted(o);
                }
                break;
            default: break;
        }
    }

}
