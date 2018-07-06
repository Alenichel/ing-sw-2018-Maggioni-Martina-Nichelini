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
    private Game lastGameReceveid = null;
    transient Scanner sinput = new Scanner(System.in);
    private ArrayList<ToolCard> toolCards;
    private boolean gameEnd = false;

    private void handleHelpCommand(){
        String help = "\n******* Help Command *******\n" +
                "- select %i: select the pattern card with the %i index.\n" +
                "- take %dieIndex %destinationX %destinationY: take the die with dieIndex and place it to the cell with the given coordinates.\n" +
                "- use %toolcardIndex: activate the toolcard with the selected index.\n" +
                "- pass: pass turn.\n" +
                "- quit: exit game and kill client." ;
        Logger.log(LoggerType.CLIENT_SIDE, NORMAL, help);
    }

    private void handleSelectCommands(String command){
        SelectionMessage sm = new SelectionMessage(Integer.valueOf(command)-1, this.client,"PatternCard");
        this.setChanged();
        this.notifyObservers(sm);
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

    private Object handleUseIO(ToolcardContent tcc, boolean amountDependentToolcard, int iteration){
        if (tcc.equals(ToolcardContent.RunBy)) return this.client.getNickname();

        else if (tcc.equals(ToolcardContent.DraftedDie)) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select a die: ");
            return Integer.parseInt(sinput.nextLine()) - 1;
        }

        else if (tcc.equals(ToolcardContent.RoundTrackDie)) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select a turn: ");
            int turn = Integer.parseInt(sinput.nextLine()) - 1;
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please select a die from that turn: ");
            int  die = Integer.parseInt(sinput.nextLine()) - 1;
            int coo[] = {turn, die};
            return coo;
        }

        else if (tcc.equals(ToolcardContent.Increase)) {
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "Please 1 to increase or 2 to decrease: ");
            return Integer.parseInt(sinput.nextLine()) == 1;
        }

        else if (tcc.equals(ToolcardContent.WindowCellStart) ||
                tcc.equals(ToolcardContent.firstWindowCellStart) ||
                (tcc.equals(ToolcardContent.secondWindowCellStart) && !amountDependentToolcard ) ||
                (tcc.equals(ToolcardContent.secondWindowCellStart) && iteration == 2 )) {
            String toLog = "Please select start window cell %x %y ";
            if (tcc.equals(ToolcardContent.firstWindowCellStart)) toLog += "(first die)";
            if (tcc.equals(ToolcardContent.secondWindowCellStart)) toLog += "(second die)";
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, toLog);
            String input =  sinput.nextLine();
            int xStart = Integer.parseInt(input.split(" ")[0])-1;
            int yStart = Integer.parseInt(input.split(" ")[1])-1;
            int[] cooStart = {xStart, yStart};
            return cooStart;
        }
        else if (tcc.equals(ToolcardContent.WindowCellEnd) ||
                tcc.equals(ToolcardContent.firstWindowCellEnd) ||
                (tcc.equals(ToolcardContent.secondWindowCellEnd) && !amountDependentToolcard) ||
                (tcc.equals(ToolcardContent.secondWindowCellEnd) && iteration == 2 )) {
            String toLog = "Please select end window cell %x %y ";
            if (tcc.equals(ToolcardContent.firstWindowCellEnd)) toLog += "(first die)";
            if (tcc.equals(ToolcardContent.secondWindowCellEnd)) toLog += "(second die)";
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, toLog);
            String input =  sinput.nextLine();
            int xEnd = Integer.parseInt(input.split(" ")[0])-1;
            int yEnd = Integer.parseInt(input.split(" ")[1])-1;
            int[] cooEnd = {xEnd, yEnd};
            return cooEnd;
        }
        else if (tcc.equals(ToolcardContent.Number)) {
            int value = 0;
            while (value < 1 || value > 6) {
                    Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "Please insert a value to assign to this die: ");
                    value = Integer.parseInt(sinput.nextLine());
            }
            return value;
        }

        else if (tcc.equals(ToolcardContent.BagDie)) {
            Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "You have taken this die form the bag --->  " + lastGameReceveid.getDieForSwitch().toString());
        }

        else if(tcc.equals(ToolcardContent.Amount)){
            int amount = 0;
            while ( amount != 1 && amount != 2) {
                Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "Please, insert the number of dice to move (1 or 2): ");
                amount = Integer.parseInt(sinput.nextLine());
            }
            return amount;
        }

        return null;
    }

    private void handleUseCommands(int n){
        ToolCard tc = toolCards.get(n-1);
        ToolCardsName tcn = tc.getToolCardName();
        boolean amountDepedentToolcard = false;
        int iteration = 0;

        Map<ToolcardContent, Object> htc= new HashMap<>();
        ToolcardContent content[] = tc.getContent();

        if (content != null) {
            for (ToolcardContent tcc : tc.getContent()) {
                if (tcc.equals(ToolcardContent.RolledNumber)) {
                    int dieIndex = (int) htc.get(ToolcardContent.DraftedDie);
                    Die die = lastGameReceveid.getDiceOnTable().get(dieIndex);
                    die.rollDice();
                    int newNumber = die.getNumber();
                    htc.put(tcc, newNumber);
                    printTable(lastGameReceveid);
                    continue;
                }
                htc.put(tcc, this.handleUseIO(tcc, amountDepedentToolcard, iteration));

                if (tcc.equals(ToolcardContent.Amount) ) {
                    amountDepedentToolcard = true;
                    iteration =  (int) htc.get(ToolcardContent.Amount);

                }
            }
        }

        ToolCardMessage tcm = new ToolCardMessage(tcn, htc);
        this.setChanged();
        this.notifyObservers(tcm);
    }

    public void run() {
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "Cli started..");
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, "*** " + this.client.getNickname() + " ***");

        loop: while (true) {
            String input = sinput.nextLine();
            String[] tokens = input.toLowerCase().split(" ");

            if (gameEnd) {

                if (Integer.parseInt(input) == 1){
                    Logger.log(LoggerType.CLIENT_SIDE, NORMAL, "Looking for another game..");
                    ConnectionMessage cm = new ConnectionMessage();
                    this.setChanged();
                    this.notifyObservers(cm);
                    gameEnd = false;
                    tokens[0] = "skip";
                }

                else if (Integer.parseInt(input) == 2){
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
                    } catch (NumberFormatException e) {Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Wrong input format, retry");}
                    break;

                case "use":
                    if (lastGameReceveid == null) {
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Game not started yet");
                        break;
                    }
                    try {
                        if (tokens.length !=  2){
                            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Wrong params insertion: USE command has the following structure:\n use %toolcardNumber");
                            break;
                        }
                        this.handleUseCommands(Integer.parseInt(tokens[1]));
                    } catch (NumberFormatException e) {
                        Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Wrong input format, retry");
                    }
                    break;

                case "help":
                    this.handleHelpCommand();
                    break;

                case "skip":
                    break;

                case "quit":
                    this.setChanged();
                    break loop;

                default:
                    Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Unrecognized command");
                    break;
            } //end while
        }
        System.exit(0);
    }

    private void requestCallback(GiveMessage callbackMessage){
        this.lastObjectReceveid = callbackMessage.getGivenObject();
        Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.NOTIFICATION, lastObjectReceveid.toString());
    }

    public void controllerCallback(Message callbackMessage){
        if (callbackMessage instanceof GiveMessage)  requestCallback((GiveMessage)callbackMessage);
        else if (callbackMessage instanceof ControllerCallbackMessage) {
            Logger.log(LoggerType.CLIENT_SIDE, ((ControllerCallbackMessage) callbackMessage).getPriority(), callbackMessage.getStringMessage());
        }
    }

    private void printTable(Game game){
        RoundTrack rT = game.getRoundTrack();
        List<WindowPatternCard> wpcs = new ArrayList<>();
        for(Player p : game.getPlayersOrder()) {
            WindowPatternCard toAdd = p.getActivePatternCard();
            if ( toAdd != null) wpcs.add(p.getActivePatternCard());
        }
        String playerName = this.activePlayer.getNickname();
        System.out.println("\n");
        System.out.print("\n");
        System.out.println("***********************************************************************************");
        System.out.println("---------> OBJECTIVE <---------");
        for (ObjectiveCard oc : game.getObjectiveCards())
            System.out.println(oc + "\n");

        System.out.println("\n***********************************************************************************");
        System.out.println("---------> TOOLCARDS <---------");
        int n = 1;
        for (ToolCard tc : game.getToolCards()) {
            System.out.print(n + ") ");
            System.out.println(tc);
            n++;
        }
        System.out.println("***********************************************************************************");

        System.out.println(rT.toString());
        if (wpcs != null && wpcs.size() != 0) ConsoleUtils.multiplePrint((ArrayList)wpcs, client);

        int i = 1;
        for (Die d : game.getDiceOnTable()) {
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
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.NORMAL, "\n\n************ You LOST ************\nThe winner is: " + game.getWinner().getNickname());
        }

        for (Player p: game.getPlayers()){
            Map<String, Integer> scoreMap = p.getScores();

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
                if(wtu.equals(WhatToUpdate.ActivePlayer) || wtu.equals(WhatToUpdate.ToolCardUpdate)) {
                    activePlayer = ((Game)o).getActivePlayer();
                    lastGameReceveid = ((Game)o);
                    printTable(lastGameReceveid);
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
