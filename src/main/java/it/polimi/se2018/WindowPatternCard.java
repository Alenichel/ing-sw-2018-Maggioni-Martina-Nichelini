package it.polimi.se2018;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WindowPatternCard extends Card {
    private WindowCell[][] grid = new WindowCell[4][5];
    private Player player;

    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String CONFIGURATION_FILENAME = "/virtus.xml";


    public WindowPatternCard() {
        try {
            loadConfiguration();
        } catch (FileNotFoundException e) {
            System.out.println("[*] Configuration file not found in " + HOME_PATH + CONFIGURATION_FILENAME + "\n[*] Aborting..");
            System.exit(1);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        for(WindowCell[] line : grid){
            for(WindowCell cell : line){
                if(cell == null){
                    cell = new WindowCell();
                }
            }
        }

    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public WindowCell getCell(int row, int column) {
        return grid[row][column];
    }

    public WindowCell[][] getGrid() {
        return this.grid;
    }

    @Override
    public String toString() {
        String string ="";
        string+= "                          \n";
        string += ""+player.getNickname()+"\n";
        for (WindowCell[] line : grid) {
            string+="--------------------------------------------\n";
            string+="|   ";
            for (WindowCell cell : line) {
                if(cell != null) {
                    if(cell.getAssignedDice() == null) {
                        //dado non insierito
                        if (cell.getColorConstraint() != null)
                            string += cell.getColorConstraint().substring(0, 1);
                        else if (cell.getNumberConstraint() != 0)
                            string += cell.getNumberConstraint();
                        else if (cell.getAssignedDice() != null)
                            string += " "+cell.getAssignedDice().getNumber()+ "  ";
                    }else{
                        //dado inserito
                        string += cell.getAssignedDice().getNumber() + cell.getAssignedDice().getColor().substring(0,1);
                    }
                }
                else string += "  ";
                string += "    |   ";
            }
            string += "\n";
        }
        string+="--------------------------------------------\n";
        string+= "                          \n";
        return string;
    }


    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
        File configurationFile = new File(HOME_PATH + CONFIGURATION_FILENAME);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(configurationFile);

        Element virtus = doc.getDocumentElement();

        List<String> numberRestrictions = Arrays.asList("one", "two", "three", "four", "five", "six");
        List<String> colorRestrictions = Arrays.asList("red", "yellow", "green", "blue", "purple");

        NodeList numberList = virtus.getElementsByTagName("number");
        NodeList colorList = virtus.getElementsByTagName("color");
        Element number = (Element) numberList.item(0);
        Element color = (Element) colorList.item(0);

        int counter = 1;
        for (String nr : numberRestrictions) {
            NodeList nRestriction = number.getElementsByTagName(nr);
            Element n = (Element) nRestriction.item(0);
            NodeList coordinateXList = n.getElementsByTagName("y");
            NodeList coordinateYList = n.getElementsByTagName("x");
            Element coordinateX = (Element) coordinateXList.item(0);
            Element coordinateY = (Element) coordinateYList.item(0);

            String xs;
            String ys;
            List<String> axs;
            List<String> ays;

            if (coordinateX != null) {
                xs = coordinateX.getFirstChild().getNodeValue();
                ys = coordinateY.getFirstChild().getNodeValue();

                axs = Arrays.asList(xs.split(" "));
                ays = Arrays.asList(ys.split(" "));


                for (int i = 0; i < axs.size(); i++) {
                    grid[Integer.parseInt(axs.get(i)) - 1][Integer.parseInt(ays.get(i)) - 1] = new WindowCell(counter);
                }
                counter++;
            }
        }

        for (String cr : colorRestrictions) {
            NodeList cRestriction = color.getElementsByTagName(cr);
            Element c = (Element) cRestriction.item(0);
            NodeList coordinateXList = c.getElementsByTagName("y");
            NodeList coordinateYList = c.getElementsByTagName("x");
            Element coordinateX = (Element) coordinateXList.item(0);
            Element coordinateY = (Element) coordinateYList.item(0);

            String xs;
            String ys;
            List<String> axs;
            List<String> ays;
            if (coordinateX != null) { //check if there are null values;
                xs = coordinateX.getFirstChild().getNodeValue();
                ys = coordinateY.getFirstChild().getNodeValue();
                axs = Arrays.asList(xs.split(" "));
                ays = Arrays.asList(ys.split(" "));

                for (int i = 0; i < axs.size(); i++) {
                    if (axs != null) {
                        grid[Integer.parseInt(axs.get(i)) - 1][Integer.parseInt(ays.get(i)) - 1] = new WindowCell(cr);
                    } //end if
                }//end for
            }//end if
        }//end method
    }


}
