package it.polimi.se2018.model;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Server class represents the server with all default params and list of active games and active players.
 */
public class Server extends Observable{

    private List<Observer> observers = new ArrayList<>();

    private int port;
    private int defaultMatchmakingTimer;
    private int defaultMoveTimer;

    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CONFIGURATION_FILENAME = "/sagrada_server_conf.xml";

    private ArrayList<Player> onlinePlayers = new ArrayList<>();
    private ArrayList<Room> activeGames = new ArrayList<>();

    private static Server instance = null;


    private Server(){
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

    /**
     * Online players getter.
     * @return List of online player.
     */
    public List<Player> getOnlinePlayers() {return this.onlinePlayers;}

    /**
     * Active games getter.
     * @return List of actve games.
     */
    public List<Room> getActiveGames() {return this.activeGames;}

    /**
     * Add room method.
     * @param gameName The name to assigne to the room.
     * @param admin The admin player (creator of the room)
     */
    public void addRoom (Room room){
            this.activeGames.add(room);
        }

    /**
     * Remove room method.
     * @param room The room to remove
     */
    public void removeRoom(Room room){
            getActiveGames().remove(room);

    }

    /**
     * Add player to the list of server active players.
     * @param player
     */
    public void addPlayer (Player player){ getOnlinePlayers().add(player);}

    /**
     * Remove player from the list of server active players.
     * @param player
     */
    public void removePlayer (Player player){
            getOnlinePlayers().remove(player);
        }

}