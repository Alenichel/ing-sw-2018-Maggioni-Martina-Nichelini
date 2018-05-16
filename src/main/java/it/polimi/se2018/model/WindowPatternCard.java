package it.polimi.se2018.model;

import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class represents the player's window
 */
public class WindowPatternCard extends Card {
    private WindowCell[][] grid = new WindowCell[4][5];

    /**
     * Class constructor
     * @param name
     */
    public WindowPatternCard(String name) {
        this.name = name;
        try {
            loadConfiguration();
        } catch (FileNotFoundException e) {
            System.exit(1);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 5; y++){
                if (grid[x][y] == null) grid[x][y] = new WindowCell(x, y);
            }
        }

        for (WindowCell[] line:grid){
            for(WindowCell cell:line) cell.setNeighbours(grid);
        }

    }

    /**
     *
     * @param row
     * @param column
     * @return The cell specified in the grid.
     */
    public WindowCell getCell(int row, int column) {
        return grid[row][column];
    }

    /**
     *
     * @return The grid
     */
    public WindowCell[][] getGrid() {
        return this.grid;
    }

    /**
     * This method load the user specified pattern card from an xml file.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
        String WINDOWSPATTERNCARD_PATH = "resources";
        File configurationFile = new File(WINDOWSPATTERNCARD_PATH + "/" + this.name + ".xml");
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
                    int x = Integer.parseInt(axs.get(i)) - 1 ;
                    int y = Integer.parseInt(ays.get(i)) - 1;
                    grid[x][y] = new WindowCell(x,y,counter);
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
            if (coordinateX != null) {
                xs = coordinateX.getFirstChild().getNodeValue();
                ys = coordinateY.getFirstChild().getNodeValue();
                axs = Arrays.asList(xs.split(" "));
                ays = Arrays.asList(ys.split(" "));

                for (int i = 0; i < axs.size(); i++) {
                    if (axs != null) {
                        int x = Integer.parseInt(axs.get(i)) - 1;
                        int y = Integer.parseInt(ays.get(i)) - 1;
                        grid[x][y] = new WindowCell(x,y,cr);
                    } //end if
                }//end for
            }//end if
        }//end method
    }

    /**
     * This method checks if it's possibile to place the given die in the selected cell looking for near dice.
     * @param windowCell
     * @param dice
     * @return True if it's a valid position, false otherwise.
     */
    private boolean isValidPosition( WindowCell windowCell, Dice dice){

        for (WindowCell wc: windowCell.getNeighbourCells()){
            if (wc.getAssignedDice() != null)
                if ( wc.getAssignedDice().getNumber()== dice.getNumber() || wc.getAssignedDice().getColor() == dice.getColor() )
                    return false;
        }
        return true;
    }

    /**
     * This method checks the window cell color and number constraints.
     * @param windowCell
     * @param dice
     * @return True if it's a valid position, false otherwise.
     */
    private boolean isValidRestriction(WindowCell windowCell, Dice dice){
        boolean colorConstraint ;
        boolean numberConstraint;

        if(dice.getNumber() != 0) numberConstraint =  !(windowCell.getNumberConstraint() == dice.getNumber());
        else numberConstraint = true;

        if(dice.getColor() != null) colorConstraint = !(windowCell.getColorConstraint() == dice.getColor());
        else colorConstraint = true;

        return colorConstraint && numberConstraint;
    }

    /**
     * Inserte die in cell if position restriction and constraints are met.
     * @param dice
     * @param row
     * @param column
     * @param checkConstraintsRestriction Set this to false if you want to ignore window cell constraints.
     * @param checkPositionRestriction Set this to true if you want to ignore position requirements.
     * @throws NotValidInsertion Thrown if requirements are not met.
     * @throws NotEmptyWindowCellException Thrown if the given cell is not empty
     */
    public void insertDice(Dice dice, int row, int column , boolean checkConstraintsRestriction, boolean checkPositionRestriction) throws NotValidInsertion, NotEmptyWindowCellException{
        boolean constraintsRestriction = false;
        boolean positionRestriction = false;

        if (checkConstraintsRestriction) constraintsRestriction = isValidRestriction(this.getCell(row,column),dice);
        else constraintsRestriction = true;

        if (checkPositionRestriction) positionRestriction = isValidPosition(this.getCell(row,column), dice);
        else positionRestriction = true;

        if (constraintsRestriction && positionRestriction) this.getCell(row, column).setAssignedDice(dice);
        else throw new NotValidInsertion("Not valid position");
    }

    @Override
    public String toString() {
        String string ="";
        string+= "                          \n";
        for (WindowCell[] line : grid) {
            string+="----------------------------------------------------------------\n";
            string += "|  ";
            for (WindowCell cell : line) {
                if(cell != null) {
                    if(cell.getAssignedDice() == null) {
                        //dado non insierito
                        if (cell.getColorConstraint() != null)
                            string += "  "+cell.getColorConstraint().substring(0, 1);
                        else if (cell.getNumberConstraint() != 0)
                            string += "  " + cell.getNumberConstraint();
                        else if (cell.getAssignedDice() != null)
                            string += cell.getAssignedDice().getNumber();
                    }else{
                        //dado inserito
                        //string += cell.getAssignedDice().getNumber() + cell.getAssignedDice().getColor().substring(0,1);
                        if(cell.getColorConstraint() != null || cell.getNumberConstraint() != 0){
                            //vincoli di colore con dado inserito
                            string += "*";
                        }
                    }
                }
                else string += "   ";
                string += "     |    ";
            }
            string += "\n";
        }
        string+="----------------------------------------------------------------\n";
        string+= "                          \n";
        return string;
    }
}
