package it.polimi.se2018;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Server {


        private int port;
        private int defaultMatchmakingTimer;
        private int defaultMoveTimer;

        private static final String HOME_PATH = System.getProperty("user.home");
        private static final String CONFIGURATION_FILENAME = "/sagrada_server_conf.xml";

        private ArrayList<Player> onlinePlayers = new ArrayList<>();
        private ArrayList<Room> activeGames = new ArrayList<>();

        private static Server instance = null;


        protected Server(){
            try {
                loadConfiguration();
            } catch (FileNotFoundException e){
                System.out.println("[*] Configuration file not found in " + HOME_PATH + CONFIGURATION_FILENAME + "\n[*] Aborting..");
                System.exit(1);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        public static Server getInstance(){
            if(instance == null){
                instance = new Server();
            }
            return instance;
        }

        private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
                File configurationFile = new File(HOME_PATH + CONFIGURATION_FILENAME);
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(configurationFile);

                this.port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
                this.defaultMatchmakingTimer = Integer.parseInt(doc.getElementsByTagName("defaultMatchmakingTimer").item(0).getTextContent());
                this.defaultMoveTimer = Integer.parseInt(doc.getElementsByTagName("defaultMoveTimer").item(0).getTextContent());
        }

        public int getDefaultMatchmakingTimer(){
                return this.defaultMatchmakingTimer;
        }

        public int getDefaultMoveTimer(){
                return this.defaultMoveTimer;
        }

        public int getServerPort() {
            return this.port;
        }

        public ArrayList<Player> getOnlinePlayers() {return this.onlinePlayers;}

        public ArrayList<Room> getActiveGames() {return this.activeGames;}

        public void addRoom (String gameName, Player admin){
            Room game = new Room(gameName, admin,false);
            getActiveGames().add(game);
        }

        public void removeRoom(Room room){
            getActiveGames().remove(room);

        }

        public void addPlayer (Player player){ getOnlinePlayers().add(player);}

        public void removePlayer (Player player){
            getOnlinePlayers().remove(player);
        }

}
