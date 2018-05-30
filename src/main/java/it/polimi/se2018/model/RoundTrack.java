package it.polimi.se2018.model;

import it.polimi.se2018.utils.ConsoleUtils;
import it.polimi.se2018.utils.DiceColor;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class RoundTrack implements Serializable{
    private ArrayList<ArrayList<Dice>> roundTrack;

    public RoundTrack(){
        roundTrack = new ArrayList<ArrayList<Dice>>();

        for(int i = 0; i<9; i++){
            roundTrack.add(i, new ArrayList<Dice>());
        }
    }

    public ArrayList<ArrayList<Dice>> getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(ArrayList<ArrayList<Dice>> roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void addDice(Dice d, int round){
        roundTrack.get(round).add(d);
    }

    public void addDice(ArrayList<Dice> d, int round){
        for(Dice p : d){
            roundTrack.get(round).add(p);
        }
    }


    @Override
    public String toString() {
        String str = "";
        int max_width = 0;
        int currentRound = 0;
        String verticalSeparatorTop = "═";
        String[] roundStr = new String[]{"R","o","u","n","d","T","r","a","c","k"};
        int roundStri = 0;
        for(ArrayList<Dice> aD : roundTrack){
            if(!aD.isEmpty()) {
                if(aD.size() > max_width)
                    max_width = aD.size();
            }
        }
        //str = str.concat(" Round tracker\n");
        str = str.concat((char) 27 +"[34m" +roundStr[roundStri] + (char) 27 + "[30m");
        roundStri++;
        str = str.concat("╔");
        for(int i = 0; i < max_width+4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╗\n");

        for(ArrayList<Dice> aD : roundTrack){
            str = str.concat((char) 27 +"[34m" +roundStr[roundStri] + (char) 27 + "[30m");
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

        //str = str.concat(verticalSeparatorBottom + "\n");
        str = str.concat(" ╚");
        for(int i = 0; i < max_width + 4; i++){
            str = str.concat(verticalSeparatorTop);
        }
        str = str.concat("╝\n");
        return str;

    }
}
