package it.polimi.se2018.model;

import it.polimi.se2018.enumeration.DiceColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements the Round track, where all remaining dice at the end of the round are placed
 */
public class RoundTrack implements Serializable{
    private List<ArrayList<Dice>> roundTrack = new ArrayList<ArrayList<Dice>>();
    private Set<DiceColor> colorSet = new HashSet<>();

    /**
     * Round track constructor
     */
    public RoundTrack(){

        for(int i = 0; i<=9; i++){
            roundTrack.add(i, new ArrayList<Dice>());
        }
    }

    /**
     * Round track getter
     * @return round track
     */
    public List<ArrayList<Dice>> getTrack() {
        return roundTrack;
    }

    /**
     * Round track setter
     * @param roundTrack list of dice on the round track
     */
    public void setRoundTrack(List<ArrayList<Dice>> roundTrack) {
        this.roundTrack = roundTrack;
    }

    /**
     * This method adds a die 'd' to the round track at the end of the round 'round'
     * @param d the die
     * @param round the round
     */
    public void addDice(Dice d, int round){
        roundTrack.get(round).add(d);
    }

    /**
     * This method adds a list of dice 'd' to the round track at the end of the round 'round'
     * @param d the list of dice
     * @param round the round
     */
    public void addDice(List<Dice> d, int round){
        for(Dice p : d){
            roundTrack.get(round).add(p);
            colorSet.add(p.getDiceColor());
        }
    }

    /**
     * Color set getter
     * @return color set
     */
    public Set<DiceColor> getColorSet() {
        return colorSet;
    }

    /**
     * To string method
     * @return string
     */
    @Override
    public String toString() {
        String str = "";
        int max_width = 0;
        int currentRound = 0;
        String verticalSeparatorTop = "═";
        String[] roundStr = new String[]{"R","o","u","n","d","T","r","a","c","k"," "};
        int roundStri = 0;
        for(List<Dice> aD : roundTrack){
            if(!aD.isEmpty()) {
                if(aD.size() > max_width)
                    max_width = aD.size();
            }
        }

        str = str.concat((char) 27 +"[34m"+ roundStr[roundStri]+ " "  + (char) 27 + "[30m");
        roundStri++;
        str = str.concat("╔");
        for(int i = 0; i < max_width+4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╗\n");

        for(List<Dice> aD : roundTrack){
            str = str.concat((char) 27 +"[34m" +roundStr[roundStri]+ " " + (char) 27 + "[30m");
            roundStri++;
            str = str.concat("║");
            int nDice = 0, nSpace = 0;
            if(!aD.isEmpty()) {
                nDice = aD.size();
                nSpace = (max_width+4-nDice-1);
                str = str.concat(" ");
                for (Dice d : aD) {
                    if (d.getNumber() != 0)
                        str = str.concat(d.toString());
                }
                for(int i = 0; i < nSpace; i++){
                    str = str.concat(" ");
                }
                currentRound++;
            }
            else{
                for(int i = 0; i < max_width+4; i++){
                    str = str.concat(" ");
                }
            }
            str = str.concat("║\n");
        }
        str = str.concat("  ╚");
        for(int i = 0; i < max_width + 4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╝\n");
        return str;

    }
}
