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

        private ArrayList<Player> onlinePlayers = new ArrayList<Player>();
        private ArrayList<GameHandler> activeGames = new ArrayList<GameHandler>();

        private static Server instance = null;


        protected Server(){
            try {
                loadConfiguration();
            } catch (FileNotFoundException e){
                System.out.println("[*] Configuration file not found in " + home_path + configurationFileName + "\n[*] Aborting..");
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

        private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException, FileNotFoundException {
                File configurationFile = new File(HOME_PATH + CONFIGURATION_FILENAME);
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(configurationFile);

                this.port = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
                this.defaultMatchmakingTimer = Integer.parseInt(doc.getElementsByTagName("defaultMatchmakinTimer").item(0).getTextContent());
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

        // this method will be called from one of the connection server
        public void addGame (String gameName, Player admin){
            GameHandler game = new GameHandler(gameName, admin,false);
            activeGames.add(game);
        }

        public void removeGame(GameHandler game){
            activeGames.remove(game);

        }

}
