package it.polimi.se2018.model;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.enumeration.WhatToUpdate;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * RMIServer class represents the server with all default params and list of active games and active players
 */
public class Server extends Observable implements Serializable {

    private int port;
    private int defaultMatchmakingTimer;
    private int defaultMoveTimer;
    private boolean configurationRequired;
    private int nOfTurn;

    private List<Player> onlinePlayers = new Vector<>();
    private List<Player> offlinePlayers = new Vector<>();
    private List<Player> inGamePlayers = new Vector<>();
    private List<Player> waitingPlayers = new Vector<>();
    private Game currentGame;

    private List<Game> activeGames = new Vector<>();

    private static Server instance = null;

    /**
     * Server constructor
     */
    private Server(){

            try {
                loadConfiguration();
            } catch (FileNotFoundException e){
                Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR ,"Configuration file not found\n[*] Aborting..");
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR ,"Configuration file not found\n[*] Aborting..");
                System.exit(1);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            }
        }

    /**
     * getInstance method compliant with Singletone Pattern
     * @return instance
     */
    public static Server getInstance(){
            if(instance == null){
                instance = new Server();
            }
            return instance;
        }


    /**
     * Configuration loader from xml configuration file
     */
    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
            InputStream xmlResource = getClass().getResourceAsStream("/sagrada_server_conf.xml");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlResource);

            this.port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
            this.defaultMatchmakingTimer = Integer.parseInt(doc.getElementsByTagName("defaultMatchmakingTimer").item(0).getTextContent());
            this.defaultMoveTimer = Integer.parseInt(doc.getElementsByTagName("defaultMoveTimer").item(0).getTextContent());
            this.configurationRequired = Boolean.parseBoolean(doc.getElementsByTagName("requirePassword").item(0).getTextContent());
            this.nOfTurn = Integer.parseInt(doc.getElementsByTagName("numberOfTurn").item(0).getTextContent());
        }

    /**
     * Default matchmaking timer getter
     * @return default matchmaking timer
     */
    public int getDefaultMatchmakingTimer(){
                return this.defaultMatchmakingTimer;
        }

    /**
     * Default move timer getter
     * @return default move timer
     */
    public int getDefaultMoveTimer(){
                return this.defaultMoveTimer;
        }

    /**
     * Boolean is configuration required
     * @return true if it's required
     */
    public boolean isConfigurationRequired() {
        return configurationRequired;
    }

    /**
     * RMIServer port getter
     * @return server port
     */
    public int getServerPort() {
            return this.port;
    }

    /**
     * Current game getter
     * @return current game
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Number of turn getter
     * @return number of turn
     */
    public int getnOfTurn() {
        return nOfTurn;
    }

    /**
     * Online players getter
     * @return list of online players
     */
    public List<Player> getOnlinePlayers() {return this.onlinePlayers;}

    /**
     * In game players getter
     * @return list of in game players
     */
    public List<Player> getInGamePlayers() {
        return inGamePlayers;
    }

    /**
     * Waiting players getter
     * @return list of waiting players
     */
    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * Active games getter
     * @return list of active games
     */
    public List<Game> getActiveGames() {
        return activeGames;
    }

    /**
     * Offline players getter
     * @return list of offline players
     */
    public List<Player> getOfflinePlayers() {
        return offlinePlayers;
    }

    /**
     * This method removes a player from the offline players and adds him to the list of online players
     * @param player added to online players
     */
    public void addPlayerToOnlinePlayers(Player player){
        if (offlinePlayers.contains(player)) offlinePlayers.remove(player);
        this.onlinePlayers.add(player);
    }

    /**
     * This method removes a player from the online players and adds him to the list of offline players
     * @param player removed from online players
     */
    public void removePlayerFromOnlinePlayers(Player player){
        this.onlinePlayers.remove(player);
        this.offlinePlayers.add(player);

        UpdateMessage um = new UpdateMessage(WhatToUpdate.PlayerStatus);
        um.setStringMessage(player.toString() + " went offline");
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * This method adds a player to the list of server active players
     * @param arrayList of active players
     * @param player to be added
     */
    public void addPlayer(List<Player> arrayList, Player player){
        arrayList.add(player);
    }


    /**
     * This method removes a player from the list of server active players
     * @param arrayList of active players
     * @param player to be removed
     */
    public void removePlayer (List<Player> arrayList, Player player){
        arrayList.remove(player);
    }

    /**
     * Current game setter
     * @param currentGame current game
     */
    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
        this.activeGames.add(currentGame);
    }

    public void setDefaultMatchmakingTimer(int defaultMatchmakingTimer) {
        this.defaultMatchmakingTimer = defaultMatchmakingTimer;
    }

    public void setDefaultMoveTimer(int defaultMoveTimer) {
        this.defaultMoveTimer = defaultMoveTimer;
    }

    public void setnOfTurn(int nOfTurn) {
        this.nOfTurn = nOfTurn;
    }

}