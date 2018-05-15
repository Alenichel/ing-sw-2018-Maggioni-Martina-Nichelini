package it.polimi.se2018;

import it.polimi.se2018.message.GetMessage;
import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.ConnectionMessage;

import java.util.*;

public class CliView extends Observable implements Observer {

    Player client;
    private Observer SCObserver;
    private Observer RCobserver;
    private Observer GCObserver;

    public CliView(Player client){
        this.client = client;
        this.SCObserver = ServerController.getInstance();
    }

    public void run(){
        Scanner sinput = new Scanner(System.in);

        while (true){
            String input = sinput.nextLine();

            if (input.equalsIgnoreCase("RequestConnection")){
                if (RCobserver != null)
                    RCobserver.update(this, new ConnectionMessage(this.client, true));
            }
            else if (input.equalsIgnoreCase("GetConnectedPlayers")){
                if (RCobserver != null)
                    RCobserver.update(this, new GetMessage("ConnectedPlayers"));
                else System.out.println("[*] ERROR: you are not connected to a room");
            }
            else if (input.equalsIgnoreCase("Quit")){
                if (RCobserver == null && GCObserver == null)
                    SCObserver.update(this, new ConnectionMessage(client, false));
                    System.out.println("[*] Goodbye");
                    break;
            }
            else {
                System.out.println("Unrecognized command");
            }
        } //end while
    }


    public void registerRCObserver(Observer o){
        this.RCobserver = o;
    }

    public void RCCallback(Message callbackMessage){
        if (callbackMessage instanceof GiveMessage){
            Object o = ((GiveMessage)callbackMessage).getGivenObject();

            if (((GiveMessage)callbackMessage).getGiving().equals("Players")){
                System.out.println(o.toString());
            }

        }
    }

    public void update(Observable o, Object msg){
        System.out.println(((Message)msg).toString());
    }
}
