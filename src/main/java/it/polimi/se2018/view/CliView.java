package it.polimi.se2018.view;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.utils.Logger;

import java.util.*;

public class CliView extends View implements Observer {

    private Object lastObjectReceveid;
    private Player player;

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
        System.out.println(this.lastObjectReceveid);
    }

    public void controllerCallback(Message callbackMessage){
        this.requestCallback((GiveMessage)callbackMessage);
    }


    public void run() {
        Logger.NOTIFICATION("Cli started..");
        Scanner sinput = new Scanner(System.in);

       System.out.println("*** " + this.player.getNickname() + " ***");

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
                        Logger.ERROR("not valid command found");
                    }
                    break;


                case "quit":
                    this.setChanged();
                    this.notifyObservers(new ConnectionMessage(client, false));
                    System.out.println("[*] Goodbye");
                    break loop;

                default:
                    Logger.ERROR("Unrecognized command");
                    break;
            } //end while
        }
    }

    public void update(Observable o, Object msg){
        switch(((Message)msg).getMessageType()){
            case "UpdateMessage":
                System.out.println(((UpdateMessage)msg));
                break;
            default: break;
        }
    }
}
