package it.polimi.se2018.view;

import it.polimi.se2018.controller.RoomController;
import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Room;
import it.polimi.se2018.model.Server;

import java.util.*;

public class CliView extends View implements Observer {

    private Observer sCObserver;
    private Observer rCObserver;
    private Observer gCObserver;

    Object lastObjectReceveid;

    public CliView(Player client){
        this.client = client;
        this.sCObserver = ServerController.getInstance();
        this.rCObserver = RoomController.getInstance();

        sCObserver.update(this, new RequestMessage("SubscribeServer"));
        sCObserver.update(this, new RequestMessage("SubscribePlayer"));

    }

    private void handleGetCommands(String command){

        switch(command){

            case "connectedplayers":
                if (this.client.getInRoom())
                    rCObserver.update(this, new RequestMessage("ConnectedPlayers"));
                else System.out.println("[*] ERROR: you are not connected to a room");
                break;

            case "activerooms":
                sCObserver.update(this, new RequestMessage("ActiveRooms"));
                break;

            default: break;
        }
    }

    private void handleRequestCommands(String command){

        switch(command){

            //toRoom
            case "connection":
                if (rCObserver != null && !this.client.getInRoom())
                    sCObserver.update(this, new ConnectionMessage(this.client, true));
                break;

            //fromRoom
            case "disconnection":
                if (rCObserver != null && this.client.getInRoom())
                    rCObserver.update(this, new ConnectionMessage(this.client, false));
                break;

            default: break;
        }
    }

    public void run() {
        System.out.println("[*] NOTIFICATION: Cli started..");
        Scanner sinput = new Scanner(System.in);

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

                case "connecttoroom":
                    if (!this.client.getInRoom()) {
                        while (true) {
                            System.out.print("[*] REQUEST: select room number: ");
                            input = sinput.nextLine();
                            if (input.equalsIgnoreCase("abort")) break;
                            int index = 0;
                            try {
                                index = Integer.parseInt(input);
                                ConnectionMessage msg = new ConnectionMessage(client, ((ArrayList<Room>) this.lastObjectReceveid).get(index - 1), true);
                                sCObserver.update(this, msg);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                                System.out.println("[*] ERROR: Not Valid Index");
                                continue;
                            }
                        }
                    }
                    else {
                        System.out.print("[*] ERROR: you are already connected to a room");
                    }
                    break;

                case "get":
                    try {
                        this.handleGetCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                        System.out.println("[*] ERROR: not valid command found");
                    }
                    break;

                case "createroom":
                    if (!client.getInRoom()) {
                        System.out.print("[*] REQUEST: insert room name: ");
                        while (true) {
                            input = sinput.nextLine();
                            if (input.equalsIgnoreCase("abort")) break;
                            sCObserver.update(this, new CreationalMessage("Room", input));
                            sCObserver.update(this, new RequestMessage("UnsubscribeServer"));
                            rCObserver.update(this, new RequestMessage("SubscribeServer"));
                            break;
                        }
                    } else {
                        System.out.println("[*] ERROR: You are already in a room");
                    }
                    break;

                case "quit":
                    if (rCObserver == null && gCObserver == null)
                        sCObserver.update(this, new ConnectionMessage(client, false));
                    System.out.println("[*] Goodbye");
                    break loop;

                default:
                    System.out.println("[*] ERROR: Unrecognized command");
                    break;
            } //end while
        }
    }

    public void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        System.out.println(this.lastObjectReceveid);
    }

    public void update(Observable o, Object msg){
        if (o instanceof Server ){

            switch(((Message)msg).getMessageType()){
                case "UpdateMessage":
                    System.out.println(((Message)msg).getStringMessage() );
                    break;
                default: break;
            }
        }
        else if (o instanceof Player ){
            this.client = (Player)o;
        }
    }
}
