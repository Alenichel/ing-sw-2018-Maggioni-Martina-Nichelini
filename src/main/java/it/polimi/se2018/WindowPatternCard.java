package it.polimi.se2018;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WindowPatternCard extends Card{
    private ArrayList<ArrayList<WindowCell>> composition;
    private Player player;
    
    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CONFIGURATION_FILENAME = "/virtus.xml";


    public WindowPatternCard (){
        try {
            loadConfiguration();
        } catch (FileNotFoundException e){
            System.out.println("[*] Configuration file not found in " + HOME_PATH + CONFIGURATION_FILENAME + "\n[*] Aborting..");
            System.exit(1);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException  {

        File configurationFile = new File(HOME_PATH + CONFIGURATION_FILENAME);
        Document doc1 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configurationFile);

        //element.add(Integer.parseInt(doc1.getElementsByTagName("three").item(0).getTextContent()));
        //element.add(Integer.parseInt(doc1.getElementsByTagName("y").item(0).getTextContent()));
        //doc1.getElementsByTagName("uno");

        //System.out.println(doc1.getElementsByTagName("four").item(0).getAttributes().getNamedItem("pos"));
        //System.out.print(doc1.getElementsByTagName("one").item(0).getTextContent().substring(9,12));
        int x = Integer.parseInt(doc1.getElementsByTagName("one").item(0).getTextContent().substring(9,10));
        int y = Integer.parseInt(doc1.getElementsByTagName("one").item(0).getTextContent().substring(11,12));

        System.out.println("x: " + x +" y: " + y);


        System.out.print(doc1.getElementsByTagName("two").item(0).getTextContent());
    }

    public WindowCell getCell(int row, int column){
        return null;
    }

}
