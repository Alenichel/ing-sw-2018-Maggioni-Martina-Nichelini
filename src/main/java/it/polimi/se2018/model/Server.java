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
    private ArrayList<Room> activeRooms = new ArrayList<>();
    private ArrayList<Game> activeGames = new ArrayList<>();

    private static Server instance = null;
    private Room room;

    private Server(){
            try {
                loadConfiguration();
                room = Room.getInstance();
                //creo una nuova partita solo nel momento in cui la precedente è piena
                if(onlinePlayers.size() > 4 * activeGames.size()){
                    activeGames.add(new Game());
                }

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
    public List<Room> getActiveRooms() {return this.activeRooms;}

    /**
     * Add room method.
     * @param gameName The name to assigne to the room.
     * @param admin The admin player (creator of the room)
     */
    public void addRoom (Room room){
        this.activeRooms.add(room);
        this.setChanged();
        UpdateMessage um = new UpdateMessage("Rooms");
        um.setStringMessage("[*] NOTIFICATION: A room has been changed");
        this.setChanged();
        this.notifyObservers(um);
    }

    /**
     * Remove room method.
     * @param room The room to remove
     */
    public void removeRoom(Room room){
            getActiveRooms().remove(room);

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
