package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.*;
import it.polimi.se2018.message.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;

import java.util.*;
import static it.polimi.se2018.enumeration.LoggerPriority.ERROR;
import static it.polimi.se2018.enumeration.LoggerPriority.NORMAL;


public class CliView extends View implements Observer {

    private transient Object lastObjectReceveid;
    private Player activePlayer = null;
    transient Scanner sinput = new Scanner(System.in);
    private ArrayList<ToolCard> toolCards;
    private boolean gameEnd = false;

    private void handleSelectCommands(String command){
        SelectionMessage sm = new SelectionMessage(Integer.valueOf(command)-1, this.client,"PatternCard");
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

    private void handleUseCommands(int n){
        ToolCardsName tc = toolCards.get(n-1).getToolCardName();
        //GrozingPliers
        if(tc.equals(ToolCardsName.GrozingPliers)){
            //Dado e booleano
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select a dice: ");
            int diceInput = Integer.parseInt(sinput.nextLine());
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please 1 to increase or 2 to decrease: ");
            boolean increase = Integer.parseInt(sinput.nextLine()) == 1;

            HashMap<ToolcardContent, Object> htc= new HashMap<ToolcardContent, Object>();
            htc.put(ToolcardContent.DraftedDie, diceInput-1);
            htc.put(ToolcardContent.Increase, increase);

            ToolCardMessage tcm = new ToolCardMessage(ToolCardsName.GrozingPliers, htc);
            this.setChanged();
            this.notifyObservers(tcm);
        }
        //EnglomiseBrush
        else if(tc.equals(ToolCardsName.EnglomiseBrush)){
            //ci servono le due coordinate e la pattern del giocatore
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select start window cell %x %y ");
            String input =  sinput.nextLine();
            int xStart = Integer.parseInt(input.split(" ")[0])-1;
            int yStart = Integer.parseInt(input.split(" ")[1])-1;
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select end window cell %x %y ");
            input =  sinput.nextLine();
            int xEnd = Integer.parseInt(input.split(" ")[0])-1;
            int yEnd = Integer.parseInt(input.split(" ")[1])-1;

            int[] cooStart = {xStart, yStart};
            int[] cooEnd = {xEnd, yEnd};

            WindowPatternCard w = getClient().getActivePatternCard();

            HashMap<ToolcardContent, Object> htc = new HashMap<>();
            htc.put(ToolcardContent.WindowCellStart, cooStart);
            htc.put(ToolcardContent.WindowCellEnd, cooEnd);
            htc.put(ToolcardContent.WindowPattern, w);

            ToolCardMessage tcm = new ToolCardMessage(ToolCardsName.EnglomiseBrush,htc);
            this.setChanged();
            this.notifyObservers(tcm);
        }else{
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION, "ERRORE");
        }


    }

    public void run() {
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Cli started..");
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "*** " + this.client.getNickname() + " ***");

        loop: while (true) {
            String input = sinput.nextLine();
            String[] tokens = input.toLowerCase().split(" ");

            if (gameEnd) {

                if (Integer.parseInt(input) == 2){
                    Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "Goodbye");
                    System.exit(0);
                    continue;
                }
                else Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\nChoose: \n\t1) Search for another game\n\t2) Quit" );
            }

            switch (tokens[0]) {

                case "request":
                    try {
                        this.handleRequestCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                        Logger.log(LoggerType.CLIENT_SIDE, ERROR, e.toString());
                    }
                    break;

                case "get":
                    try {
                        this.handleGetCommands(tokens[1]);
                    } catch (IndexOutOfBoundsException e){
                        Logger.log(LoggerType.CLIENT_SIDE, ERROR, "not valid command found");
                    }
                    break;

                case "select":
                    if (tokens.length == 1) {
                        Logger.log(LoggerType.CLIENT_SIDE, ERROR, "argument is missing");
                        break;
                    }
                    handleSelectCommands(tokens[1]);
                    break;

                case "pass":
                    this.setChanged();
                    this.notifyObservers(new UpdateMessage(WhatToUpdate.Pass));
                    break;

                case "take":
                    try {
                        if (tokens.length < 4){
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Wrong params insertion: TAKE command has the following structure:\n take %diceNumber %targetX %targetY");
                            break;
                        }
                        this.handleTakeCommands(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                    } catch (NumberFormatException e) {Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, "Wrong input format, retry");}
                    break;

                case "use":
                    try {
                        if (tokens.length < 2){
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Wrong params insertion: USE command has the following structure:\n use %toolcardNumber");
                            break;
                        }
                        this.handleUseCommands(Integer.parseInt(tokens[1]));
                    } catch (NumberFormatException e) {Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, "Wrong input format, retry");}
                    break;

                case "quit":
                    this.setChanged();
                    this.notifyObservers(new ConnectionMessage(client, false));
                    break loop;

                default:
                    Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, "Unrecognized command");
                    break;
            } //end while
        }
    }

    private void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, lastObjectReceveid.toString());
    }

    public void controllerCallback(Message callbackMessage){
        if (callbackMessage instanceof GiveMessage)  requestCallback((GiveMessage)callbackMessage);
        else if (callbackMessage instanceof ControllerCallbackMessage)
            Logger.log(LoggerType.CLIENT_SIDE, ((ControllerCallbackMessage)callbackMessage).getPriority(),callbackMessage.getStringMessage());
    }

    private void printTable(Game game, Message msg){

        RoundTrack rT = game.getRoundTrack();
        List<WindowPatternCard> wpcs = new ArrayList<>();
        for(Player p : game.getPlayers()) wpcs.add(p.getActivePatternCard());
        String tokens[] = ((UpdateMessage)msg).toString().split(":");
        String playerName = tokens[1];
        System.out.println("\n");
        System.out.print("\n");
        System.out.println("***********************************************************************************");
        System.out.println("---------> OBJECTIVE <---------");
        for (ObjectiveCard oc : game.getObjectiveCards())
            System.out.println(oc);

        System.out.println("\n***********************************************************************************");
        System.out.println("---------> TOOLCARDS <---------");
        for (ToolCard tc : game.getToolCards())
            System.out.println(tc);
        System.out.println("***********************************************************************************");

        System.out.println(rT.toString());
        ConsoleUtils.multiplePrint((ArrayList)wpcs, client);

        int i = 1;
        for (Dice d : game.getDiceOnTable()) {
            System.out.print( i + ") " +d.toString() + " ");
            i++;
        }
        System.out.println("");
        if(playerName.equals(client.getNickname())) System.out.println((char) 27 +"[34m" + "Is your turn!" + (char) 27 + "[30m");
        else System.out.println("This is the turn of player: " + playerName);
    }

    private void onGameStarted(Observable o){
        Game game = ((Game)o);
        int i = 1;
        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Select one these cards : \n");

        ArrayList<WindowPatternCard> pool = null;
        for(Player p : game.getPlayers()){
            if(p.getNickname().equals(client.getNickname())) {
                pool = (ArrayList<WindowPatternCard>)p.getWindowPatternCardsPool();
                for (WindowPatternCard w : pool) {
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,((Integer) i).toString() + ") " + w.getName());
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL,("Number of favor tokens : " + w.getNumberOfFavorTokens()) + "\n");
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, w.toString());
                    i++;
                }
            }
        }
    }

    private void onWinnerProclamation(Game game){
        if (game.getWinner().getNickname().equals(this.client.getNickname())){
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\n\n************ You WON ************");
        }
        else {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\n\n************ You LOSE ************\nThe winner is: " + game.getWinner().getNickname());
        }

        for (Player p: game.getPlayers()){
            HashMap<String, Integer> scoreMap = p.getScores();

            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\nPlayer: " + p.getNickname() + " (Score " + p.getScore() + ")");
            for (String s: scoreMap.keySet()){
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\t- " + s + ": " + scoreMap.get(s));
            }
        }

        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\nChoose: \n\t1) Search for another game\n\t2) Quit" );
        this.gameEnd = true;
    }

    public void update(Observable o, Object msg){
        if(o instanceof Game){
            for(Player p : ((Game) o).getPlayers())
                if(p.getNickname().equals(client.getNickname()))
                    client = p;
        }
        switch(((Message)msg).getMessageType()){
            case "UpdateMessage":
                WhatToUpdate wtu = ((UpdateMessage)msg).getWhatToUpdate();
                if(wtu.equals(WhatToUpdate.ActivePlayer)) {
                    printTable((Game)o, (Message) msg);
                }

                else{
                    if(wtu.equals(WhatToUpdate.GameStarted)) {
                        toolCards = (ArrayList<ToolCard>)((Game)o).getToolCards();
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION, msg.toString());
                        onGameStarted(o);
                    } else if(wtu.equals(WhatToUpdate.Winner)) {
                        this.onWinnerProclamation((Game)o);
                    } else if(wtu.equals(WhatToUpdate.TimeLeft)){
                        if(!((Game)o).isStarted()){
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION ,msg.toString());
                        }
                    }
                    else {
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NOTIFICATION ,msg.toString());
                    }
                }

                break;
            default: break;
        }
    }

}
