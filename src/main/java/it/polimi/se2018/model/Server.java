package it.polimi.se2018.model;
import it.polimi.se2018.message.UpdateMessage;
import it.polimi.se2018.message.WhatToUpdate;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.LoggerPriority;
import it.polimi.se2018.utils.LoggerType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.text.StyledEditorKit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * RMIServer class represents the server with all default params and list of active games and active players.
 */
public class Server extends Observable implements Serializable {

    private int port;
    private int defaultMatchmakingTimer;
    private int defaultMoveTimer;
    private boolean configurationRequired;
    private int nOfTurn;

    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CONFIGURATION_FILENAME = "/sagrada_server_conf.xml";

    private List<Player> onlinePlayers = new Vector<>();
    private List<Player> offlinePlayers = new Vector<>();
    private List<Player> inGamePlayers = new Vector<>();
    private List<Player> waitingPlayers = new Vector<>();
    private Game currentGame;

    private List<Game> activeGames = new Vector<>();

    private static Server instance = null;


    private Server(){

            try {
                loadConfiguration();
            } catch (FileNotFoundException e){
                System.out.println("[*] Configuration file not found in " + HOME_PATH + CONFIGURATION_FILENAME + "\n[*] Aborting..");
                System.exit(1);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
            }
        }

    /**
     * getInstance method compliant with Singletone Pattern
     * @return
     */
    public static Server getInstance(){
            if(instance == null){
                instance = new Server();
            }
            return instance;
        }


    /**
     * Configuration loader from xml conf file.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
                File configurationFile = new File("resources" + CONFIGURATION_FILENAME);
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(configurationFile);

                this.port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
                this.defaultMatchmakingTimer = Integer.parseInt(doc.getElementsByTagName("defaultMatchmakingTimer").item(0).getTextContent());
                this.defaultMoveTimer = Integer.parseInt(doc.getElementsByTagName("defaultMoveTimer").item(0).getTextContent());
                this.configurationRequired = Boolean.parseBoolean(doc.getElementsByTagName("requirePassword").item(0).getTextContent());
                this.nOfTurn = Integer.parseInt(doc.getElementsByTagName("numberOfTurn").item(0).getTextContent());;
        }

    /**
     * Default matchmaking timer getter.
     * @return Default matchmaking timer
     */
    public int getDefaultMatchmakingTimer(){
                return this.defaultMatchmakingTimer;
        }

    /**
     * Default move timer getter.
     * @return defualt move timer.
     */
    public int getDefaultMoveTimer(){
                return this.defaultMoveTimer;
        }

    public boolean isConfigurationRequired() {
        return configurationRequired;
    }

    /**
     * RMIServer port getter.
     * @return server port.
     */
    public int getServerPort() {
            return this.port;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public int getnOfTurn() {
        return nOfTurn;
    }

    /**
     * Online players getter.
     * @return List of online player.
     */
    public List<Player> getOnlinePlayers() {return this.onlinePlayers;}

    public List<Player> getInGamePlayers() {
        return inGamePlayers;
    }

    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public List<Game> getActiveGames() {
        return activeGames;
    }

    public List<Player> getOfflinePlayers() {
        return offlinePlayers;
    }

    public void addPlayerToOnlinePlayers(Player player){
        if (offlinePlayers.contains(player)) offlinePlayers.remove(player);
        this.onlinePlayers.add(player);
    }

    public void removePlayerFromOnlinePlayers(Player player){
        this.onlinePlayers.remove(player);
        this.offlinePlayers.add(player);

        UpdateMessage um = new UpdateMessage(WhatToUpdate.PlayerStatus);
        um.setStringMessage(player.toString() + " went offline");
        this.setChanged();
        this.notifyObservers(um);
    }

    public void addPlayer(List<Player> arrayList, Player player){
        arrayList.add(player);
    }


    /**
     * Remove player from the list of server active players.
     * @param player
     */
    public void removePlayer (List<Player> arrayList, Player player){
        arrayList.remove(player);
    }


    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
        this.activeGames.add(currentGame);
    }
}
