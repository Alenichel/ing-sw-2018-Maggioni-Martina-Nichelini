package it.polimi.se2018.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class RoundTrack implements Serializable{
    private ArrayList<Dice> roundTrack = new ArrayList<>();

    public ArrayList<Dice> getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(ArrayList<Dice> roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void addDice(Dice d){
        roundTrack.add(d);
    }

    public void moveDice(int round, Dice d){
        roundTrack.remove(round);
        roundTrack.add(round, d);
    }


    @Override
    public String toString() {
        String str = "";
        int currentRound = roundTrack.size();
        String verticalSeparatorTop = "\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581";
        String verticalSeparatorBottom = "\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594";

        str = str.concat(verticalSeparatorTop + "\n");
        str = str.concat("|");
        for(Dice d : roundTrack){
           str = str.concat(d.toString());
       }
       for(int i = currentRound+1; i<11; i++){
           str = str.concat("\u2610");
       }
        str = str.concat("|\n");
        str = str.concat(verticalSeparatorBottom + "\n");

        return str;

    }
}
