package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoundTrack implements Serializable{
    private List<ArrayList<Dice>> roundTrack = new ArrayList<>();

    public RoundTrack(){

        for(int i = 0; i<=9; i++){
            roundTrack.add(i, new ArrayList<Dice>());
        }
    }

    public List<ArrayList<Dice>> getTrack() {
        return roundTrack;
    }

    public void setRoundTrack(List<ArrayList<Dice>> roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void addDice(Dice d, int round){
        roundTrack.get(round).add(d);
    }

    public void addDice(List<Dice> d, int round){
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
