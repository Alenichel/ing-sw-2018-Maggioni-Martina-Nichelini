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
    private Scanner sinput = new Scanner(System.in);


    public void setPlayer(Player player) {
        this.player = player;
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

    public void cliGame(Observable o){


        Game game = ((Game)o);
        ArrayList<Player> players = new ArrayList<>(game.getPlayers());
        int i = 1;
        /*for(Player p : players ){
            Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, p.getNickname());
            Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, ( (Integer) p.getNumberOfFavorTokens() ).toString());
        }*/

        Logger.log(LoggerType.CLIENT_SIDE, "Select one these cards : ");

        for(WindowPatternCard w : player.getWindowPatternCardsPool()){
            Logger.log(LoggerType.CLIENT_SIDE, ((Integer) i).toString());
            Logger.log(LoggerType.CLIENT_SIDE, w.toString());
        }
        String input = sinput.nextLine();
        this.setChanged();

        //dovrei dirgli che sto notificando questo????
        //che messaggio usiamo??
        //aggiungo parametro??
        //------->ho aggiunto un costruttore in SelectionMessage
        this.notifyObservers(new SelectionMessage(((Integer)(Integer.parseInt(input) -1 )).toString(), player.getPlayerNumber(), "PatternCard"));

    }




    public void update(Observable o, Object msg){
        switch(((Message)msg).getMessageType()){
            case "UpdateMessage":
                String wtu = ((UpdateMessage)msg).getWhatToUpdate();
                Logger.NOTIFICATION(LoggerType.CLIENT_SIDE,msg.toString());
                if(wtu.equals("GameStarted")) {
                    cliGame(o);
                }
                break;
            default: break;
        }
    }


}
