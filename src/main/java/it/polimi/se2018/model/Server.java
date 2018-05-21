package it.polimi.se2018.model;
import it.polimi.se2018.message.UpdateMessage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Server class represents the server with all default params and list of active games and active players.
 */
public class Server extends Observable implements Serializable {

    private int port;
    private int defaultMatchmakingTimer;
    private int defaultMoveTimer;

    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CONFIGURATION_FILENAME = "/sagrada_server_conf.xml";

    private ArrayList<Player> onlinePlayers = new ArrayList<>();
    private ArrayList<Player> inGamePlayers = new ArrayList<>();
    private ArrayList<Player> waitingPlayers = new ArrayList<>();
    private Game currentGame;

    private ArrayList<Game> activeGames = new ArrayList<>();

    private static Server instance = null;

    private Server(){
        this.currentGame = new Game();
            try {
                loadConfiguration();
            } catch (FileNotFoundException e){
                System.out.println("[*] Configuration file not found in " + HOME_PATH + CONFIGURATION_FILENAME + "\n[*] Aborting..");
                System.exit(1);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
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
                File configurationFile = new File(HOME_PATH + CONFIGURATION_FILENAME);
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(configurationFile);

                this.port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
                this.defaultMatchmakingTimer = Integer.parseInt(doc.getElementsByTagName("defaultMatchmakingTimer").item(0).getTextContent());
                this.defaultMoveTimer = Integer.parseInt(doc.getElementsByTagName("defaultMoveTimer").item(0).getTextContent());
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

    /**
     * Server port getter.
     * @return server port.
     */
    public int getServerPort() {
            return this.port;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Online players getter.
     * @return List of online player.
     */
    public List<Player> getOnlinePlayers() {return this.onlinePlayers;}

    public ArrayList<Player> getInGamePlayers() {
        return inGamePlayers;
    }

    public ArrayList<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public ArrayList<Game> getActiveGames() {
        return activeGames;
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

}
