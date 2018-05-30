package it.polimi.se2018.view;

import it.polimi.se2018.message.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerType;

import java.util.*;



public class CliView extends View implements Observer {

    private Player player;
    private transient Object lastObjectReceveid;
    private Player activePlayer;

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

    private void handleTakeCommands(int n, int endingX, int endingY){
        MoveDiceMessage mdm = new MoveDiceMessage(n, endingX-1, endingY-1);
        this.setChanged();
        this.notifyObservers(mdm);
    }

    public void run() {
        Scanner sinput = new Scanner(System.in);
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, "Cli started..");
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, "*** " + this.player.getNickname() + " ***");

        loop: while (true) {
            String input = sinput.nextLine();
            String[] tokens = input.toLowerCase().split(" ");

            if (!this.client.getNickname().equals(this.activePlayer.getNickname()) ) {
                Logger.ERROR(LoggerType.CLIENT_SIDE, "This not your turn");
                continue;
            }

            switch (tokens[0]) {

                case "request":
                    try {
                        this.handleRequestCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                        Logger.ERROR(LoggerType.CLIENT_SIDE, e.toString());
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

                case "pass":
                    this.setChanged();
                    this.notifyObservers(new UpdateMessage("Pass"));
                    break;

                case "take":
                    try {
                        this.handleTakeCommands(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                    } catch (NumberFormatException e) {Logger.ERROR(LoggerType.CLIENT_SIDE, "Wrong input format, retry");}
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

    private void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, lastObjectReceveid.toString());
    }

    public void controllerCallback(Message callbackMessage){
        if (callbackMessage instanceof GiveMessage)  requestCallback((GiveMessage)callbackMessage);
        else Logger.NOTIFICATION(LoggerType.CLIENT_SIDE, callbackMessage.getStringMessage());
    }

    private void printTable(Game game, Message msg){

        RoundTrack rT = game.getRoundTrack();
        List<WindowPatternCard> wpcs = new ArrayList<>();
        for(Player p : game.getPlayers()) wpcs.add(p.getActivePatternCard());
        String tokens[] = ((UpdateMessage)msg).toString().split(":");
        String playerName = tokens[1];
        System.out.println("\n");
        System.out.print("\n");
        System.out.println("***********************************************************************************\n---------> OBJECTIVE <---------");
        for (ObjectiveCard oc : game.getObjectiveCards())
            System.out.println(oc);

        System.out.println("\n***********************************************************************************\n---------> TOOLCARDS <---------");
        for (ToolCard tc : game.getToolCards())
            System.out.println(tc);
        System.out.println("***********************************************************************************");

        System.out.println(rT.toString());
        ConsoleUtils.multiplePrint((ArrayList)wpcs, player);

        int i = 1;
        for (Dice d : game.getDiceOnTable()) {
            System.out.print( i + ") " +d.toString() + " ");
            i++;
        }
        System.out.println("");
        if(playerName.equals(player.getNickname())) System.out.println((char) 27 +"[34m" + "Is your turn!" + (char) 27 + "[30m");
        else System.out.println("This is the turn of player:" + playerName);
    }

    private void onGameStarted(Observable o){
        Game game = ((Game)o);
        int i = 1;
        Logger.log(LoggerType.CLIENT_SIDE, "Select one these cards : \n");

        ArrayList<WindowPatternCard> pool = null;
        for(Player p : game.getPlayers()){
            if(p.getNickname().equals(player.getNickname())) {
                pool = (ArrayList<WindowPatternCard>)p.getWindowPatternCardsPool();
                for (WindowPatternCard w : pool) {
                    Logger.log(LoggerType.CLIENT_SIDE, ((Integer) i).toString() + ") " + w.getName());
                    Logger.log(LoggerType.CLIENT_SIDE, ("Number of favor tokens : " + w.getNumberOfFavorTokens()) + "\n");
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
                if(wtu.equals("ActivePlayer")) printTable((Game)o, (Message) msg);
                else{
                    Logger.NOTIFICATION(LoggerType.CLIENT_SIDE,msg.toString());
                    if(wtu.equals("GameStarted")) {
                        onGameStarted(o);
                    }else if(wtu.equals("ActivePlayer")) {
                        this.activePlayer = ((Game)o).getActivePlayer();
                        Logger.NOTIFICATION(LoggerType.CLIENT_SIDE,msg.toString());
                    }
                }

                break;
            default: break;
        }
    }

}
