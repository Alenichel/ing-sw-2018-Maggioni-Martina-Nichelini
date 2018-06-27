package it.polimi.se2018.model;

import it.polimi.se2018.exception.GameException;
import it.polimi.se2018.exception.NotEmptyWindowCellException;
import it.polimi.se2018.exception.NotValidInsertion;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.enumeration.WindowPatternCardsName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class represents the player's window
 */
public class WindowPatternCard extends Card implements Serializable {
    private WindowCell[][] grid = new WindowCell[4][5];
    private WindowPatternCardsName wpname;
    private int numberOfFavorTokens;
    private Player player;

    private int placedDice = 0;

    /**
     * Class constructor
     * @param name window pattern card name
     */
    public WindowPatternCard(WindowPatternCardsName name) {
        this.wpname = name;
        this.name = name.toString();
        try {
            loadConfiguration();
        } catch (FileNotFoundException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, e.toString());
            System.exit(1);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            Logger.log(LoggerType.SERVER_SIDE, LoggerPriority.ERROR, Arrays.toString(e.getStackTrace()));
        }

        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 5; y++){
                if (grid[x][y] == null) grid[x][y] = new WindowCell(x, y);
            }
        }

        for (WindowCell[] line:grid){
            for(WindowCell cell:line) {
                cell.setNeighbours(grid);
                cell.setDiagonals(grid);
            }
        }

    }

    /**
     * Cell getter
     * @param row
     * @param column
     * @return the specified cell in the grid
     */
    public WindowCell getCell(int row, int column) {
        return grid[row][column];
    }

    /**
     * Grid getter
     * @return The grid
     */
    public WindowCell[][] getGrid() {
        return this.grid;
    }

    /**
     * Number of favor tokens getter
     * @return number of favor tokens of the window pattern card
     */
    public int getNumberOfFavorTokens() {
        return numberOfFavorTokens;
    }

    /**
     * Name getter
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Window pattern card name getter
     * @return window pattern card name
     */
    public WindowPatternCardsName getWPName() {
        return wpname;
    }

    /**
     * Player getter
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Player setter
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * This method loads the user specified pattern card from an xml file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void loadConfiguration() throws ParserConfigurationException, IOException, SAXException {
        InputStream xmlResource = getClass().getResourceAsStream("/patternCards" + "/" + this.name + ".xml");
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(xmlResource);

        Element virtus = doc.getDocumentElement();

        NodeList appoNodeList = virtus.getElementsByTagName("nOfFavorTokens");
        Element appoElement = (Element) appoNodeList.item(0);
        this.numberOfFavorTokens = Integer.parseInt(appoElement.getFirstChild().getNodeValue());


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
     * Placed dice getter
     * @return placed dice
     */
    public int getPlacedDice() {
        return placedDice;
    }

    /**
     * This method allows the player to use a favor token
     * @param n Number of tokens to use
     * @throws GameException if the player doesn't have enough tokens
     */
    public void useToken(int n) throws GameException {
        if (numberOfFavorTokens >=  0 + n) numberOfFavorTokens -= n;
        else throw new GameException("NotEnoughTokens");
    }

    /**
     * This method checks if it's possible to place the given die on the selected cell looking for near dice
     * @param windowCell
     * @param dice
     * @return true if it's a valid position, false otherwise
     */
    private boolean isValidPosition( WindowCell windowCell, Dice dice){
        if (placedDice == 0) return true;
        int counterNearDice = 0;

        for (WindowCell wc : windowCell.getDiagonalCells())
            if (wc.getAssignedDice() != null) counterNearDice++;

        for (WindowCell wc: windowCell.getNeighbourCells()){
            if (wc.getAssignedDice() != null) {
                counterNearDice++;
                if (wc.getAssignedDice().getNumber() == dice.getNumber() || wc.getAssignedDice().getColor().equals(dice.getColor()))
                    return false;
            }
        }
        if (counterNearDice != 0) return true;
        return false;
    }

    /**
     * This method checks the window cell color constraint
     * @param windowCell
     * @param dice
     * @return true if it's a valid position, false otherwise
     */
    private boolean isValidColorRestriction(WindowCell windowCell, Dice dice){
        boolean colorConstraint;

        if(dice.getColor() != null && windowCell.getColorConstraint() != null)
            colorConstraint = (windowCell.getColorConstraint().equals(dice.getColor()));
        else colorConstraint = true;

        return colorConstraint;
    }

    /**
     * This method checks the window cell number constraint
     * @param windowCell
     * @param dice
     * @return true if it's a valid position, false otherwise
     */
    private boolean isValidNumberRestriction(WindowCell windowCell, Dice dice){
        boolean numberConstraint;

        if(dice.getNumber() != 0 && windowCell.getNumberConstraint() != 0)
            numberConstraint =  (windowCell.getNumberConstraint() == dice.getNumber());
        else numberConstraint = true;

        return numberConstraint;
    }

    /**
     * This method checks for all the restriction and if they are met, inserts a die.
     * @param dice the die to be inserted
     * @param row row index
     * @param column column index
     * @param checkColorRestriction set this to false if you want to ignore window cell color constraints
     * @param checkNumberRestriction set this to false if you want to ignore window cell number constraints
     * @param checkPositionRestriction set this to true if you want to ignore position requirements
     * @throws NotValidInsertion if requirements are not met
     * @throws NotEmptyWindowCellException thrown if the given cell is not empty
     */
    public void insertDice(Dice dice, int row, int column , boolean checkColorRestriction, boolean checkNumberRestriction, boolean checkPositionRestriction) throws NotValidInsertion, NotEmptyWindowCellException{
        boolean colorRestriction = false;
        boolean numberRestriction = false;
        boolean positionRestriction = false;

        if (checkColorRestriction) colorRestriction = isValidColorRestriction(this.getCell(row,column),dice);
        else colorRestriction = true;

        if (checkNumberRestriction) numberRestriction = isValidNumberRestriction(this.getCell(row,column),dice);
        else numberRestriction = true;

        if (checkPositionRestriction) positionRestriction = isValidPosition(this.getCell(row,column), dice);
        else positionRestriction = true;

        if (colorRestriction && numberRestriction && positionRestriction) {
            this.getCell(row, column).setAssignedDice(dice);
            this.placedDice++;
        }
        else throw new NotValidInsertion("Not valid position");
    }

    @Override
    public String toString() {
        String string ="";
        int row = 1;
        String firstverticalSeparator = "\u250F\u2501\u2501\u2501\u2501\u2501\u2533\u2501\u2501\u2501\u2501\u2501\u2533\u2501\u2501\u2501\u2501\u2501\u2533\u2501\u2501\u2501\u2501\u2501\u2533\u2501\u2501\u2501\u2501\u2501\u2513";
        String verticalSeparator = "\u2523\u2501\u2501\u2501\u2501\u2501\u254B\u2501\u2501\u2501\u2501\u2501\u254B\u2501\u2501\u2501\u2501\u2501\u254B\u2501\u2501\u2501\u2501\u2501\u254B\u2501\u2501\u2501\u2501\u2501\u252B";
        String lastverticalSeparator = "\u2517\u2501\u2501\u2501\u2501\u2501\u253B\u2501\u2501\u2501\u2501\u2501\u253B\u2501\u2501\u2501\u2501\u2501\u253B\u2501\u2501\u2501\u2501\u2501\u253B\u2501\u2501\u2501\u2501\u2501\u251B";

        String horizontalSeparator = "\u2503";
        final String BACK_TO_BLACK = (char) 27 + "[30m";
        string = string.concat((char) 27 + "[32m");
        try {
            string = string.concat("\" "+player.getNickname()+" \""+" - tokens: "+ BACK_TO_BLACK+player.getActivePatternCard().getNumberOfFavorTokens()+ " \t\t\t\t\n");
        }catch (NullPointerException e){; }
        string = string.concat((char) 27 + "[31m");
        string = string.concat("     1     2     3     4     5     \n" + BACK_TO_BLACK);
        for (WindowCell[] line : grid) {
            if(row == 1)
                string = string.concat("  "+firstverticalSeparator+"  \n");
            else
                string = string.concat("  "+verticalSeparator + "  \n" );

            string = string.concat((char) 27 + "[31m");
            string = string.concat(((Integer)row).toString()+ BACK_TO_BLACK);
            string = string.concat(" "+horizontalSeparator+ "  " );
            for (WindowCell cell : line) {
                    if(cell.getAssignedDice() == null) {
                        //constraint or empty cell
                        if (cell.getColorConstraint() != null){
                            //color constraint
                            string = string.concat(toUnicodeColor(cell.getColorConstraint()));
                            string = string.concat("\u25FE");
                            string = string.concat(BACK_TO_BLACK);
                            string = string.concat("  "+horizontalSeparator+"  ");
                        }
                        else if (cell.getNumberConstraint() != 0){
                            //number constraint
                            string = string.concat(((Integer)cell.getNumberConstraint()).toString());
                            string = string.concat("  "+horizontalSeparator+"  ");
                        }
                        else {
                            //empty cell
                            string = string.concat(" ");
                            string = string.concat("  " + horizontalSeparator + "  ");

                        }
                    }else{
                        // not empty cell
                        string = string.concat(cell.getAssignedDice().toString());

                        if(cell.getColorConstraint() != null || cell.getNumberConstraint() != 0){
                            //color or number constraint with a die
                            string = string.concat("*");
                            string = string.concat(" "+horizontalSeparator+"  ");

                        }else{
                            string = string.concat("  "+horizontalSeparator+"  ");

                        }
                    }
            }
            string = string.concat("\n");
            row++;
        }
        string = string.concat("  "+lastverticalSeparator + "  \n" );
        return string;
    }

    private String toUnicodeColor(String color){
        switch (color.toLowerCase()){
            case "red":         return (char) 27 + "[31m";
            case "yellow":      return (char) 27 + "[33m";
            case "green":       return (char) 27 + "[32m";
            case "blue":        return (char) 27 + "[34m";
            case "purple":      return (char) 27 + "[35m";
            default: return "";
        }
    }
}
