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
    private List<ArrayList<Die>> track = new ArrayList<>();
    private Set<DiceColor> colorSet = new HashSet<>();

    /**
     * Round track constructor
     */
    public RoundTrack(){

        for(int i = 0; i<=9; i++){
            track.add(i, new ArrayList<Die>());
        }
    }

    /**
     * Round track getter
     * @return round track
     */
    public List<ArrayList<Die>> getTrack() {
        return track;
    }

    /**
     * Round track setter
     * @param track list of dice on the round track
     */
    public void setTrack(List<ArrayList<Die>> track) {
        this.track = track;
    }

    /**
     * This method adds a die 'd' to the round track at the end of the round 'round'
     * @param d the die
     * @param round the round
     */
    public void addDice(Die d, int round){
        track.get(round).add(d);
    }

    /**
     * This method adds a list of dice 'd' to the round track at the end of the round 'round'
     * @param d the list of dice
     * @param round the round
     */
    public void addDice(List<Die> d, int round){
        for(Die p : d){
            track.get(round).add(p);
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
        int maxWidth = 0;
        String verticalSeparatorTop = "═";
        String[] roundStr = new String[]{"R","o","u","n","d","T","r","a","c","k"," "};
        int roundStri = 0;
        for(List<Die> aD : track){
            if(!aD.isEmpty() && aD.size() > maxWidth) maxWidth = aD.size();
        }

        str = str.concat((char) 27 +"[34m"+ roundStr[roundStri]+ " "  + (char) 27 + "[30m");
        roundStri++;
        str = str.concat("╔");
        for(int i = 0; i < maxWidth+4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╗\n");

        for(List<Die> aD : track){
            str = str.concat((char) 27 +"[34m" +roundStr[roundStri]+ " " + (char) 27 + "[30m");
            roundStri++;
            str = str.concat("║");
            int nDice = 0;
            int nSpace = 0;
            if(!aD.isEmpty()) {
                nDice = aD.size();
                nSpace = (maxWidth+4-nDice-1);
                str = str.concat(" ");
                for (Die d : aD) {
                    if (d.getNumber() != 0)
                        str = str.concat(d.toString());
                }
                for(int i = 0; i < nSpace; i++){
                    str = str.concat(" ");
                }
            }
            else{
                for(int i = 0; i < maxWidth+4; i++){
                    str = str.concat(" ");
                }
            }
            str = str.concat("║\n");
        }
        str = str.concat("  ╚");
        for(int i = 0; i < maxWidth + 4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╝\n");
        return str;

    }
}