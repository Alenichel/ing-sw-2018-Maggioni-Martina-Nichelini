package it.polimi.se2018;

import it.polimi.se2018.message.RequestMessage;
import it.polimi.se2018.message.GiveMessage;
import it.polimi.se2018.message.Message;
import it.polimi.se2018.message.ConnectionMessage;

import java.util.*;

public class CliView extends View implements Observer {

    Player client;
    private Observer SCObserver;
    private Observer RCobserver;
    private Observer GCObserver;

    Object lastObjectReceveid;

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
            else if (input.equalsIgnoreCase("Connect")){
                while (true) {
                    input = sinput.nextLine();
                    if (input.equalsIgnoreCase("abort")) break;
                    int index = 0;
                    try {
                        index = Integer.parseInt(input);
                        ConnectionMessage msg = new ConnectionMessage(client, ((ArrayList<Room>) this.lastObjectReceveid).get(index-1), true);
                        SCObserver.update(this, msg);
                    } catch (NumberFormatException |  IndexOutOfBoundsException e){
                        System.out.println("Not Valid Index");
                        continue;
                    }
                    break;
                }
            }
            else if (input.equalsIgnoreCase("GetConnectedPlayers")){
                if (RCobserver != null)
                    RCobserver.update(this, new RequestMessage("ConnectedPlayers"));
                else System.out.println("[*] ERROR: you are not connected to a room");
            }
            else if (input.equalsIgnoreCase("GetActiveRooms")){
                SCObserver.update(this, new RequestMessage("ActiveRooms"));
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

    public void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        System.out.println(this.lastObjectReceveid);
    }







    public void update(Observable o, Object msg){
        System.out.println(((Message)msg).toString());
    }
}
