package it.polimi.se2018.utils;

        import it.polimi.se2018.model.WindowPatternCard;

        import java.util.ArrayList;
        import java.util.List;

public class ConsoleUtils {
    public static void multiplePrint(ArrayList<WindowPatternCard> ws) {
        ArrayList<String[]> tokensTOT = new ArrayList<>();

        int i = 0, j = 0;
        for (WindowPatternCard w : ws) {
            tokensTOT.add(ws.get(i).toString().split("\n"));
            i++;
        }

        for(int a = 0; a < tokensTOT.get(0).length; a++){
            for(int b = 0; b < tokensTOT.size(); b++){
                System.out.print(tokensTOT.get(b)[a] + "   ");
            }
            System.out.println();
        }
    }

    public String toUnicodeColor(String color){
        switch (color.toLowerCase()){
            case "red":         return (char) 27 + "[31m";
            case "yellow":      return (char) 27 + "[33m";
            case "green":       return (char) 27 + "[32m";
            case "blue":        return (char) 27 + "[34m";
            case "purple":      return (char) 27 + "[35m";
            default: return "";
        }
    }

    public String toUnicodeNumber(int number){
        switch (number){
            case 1:    return "\u2680";
            case 2:    return "\u2681";
            case 3:    return "\u2682";
            case 4:    return "\u2683";
            case 5:    return "\u2684";
            case 6:    return "\u2685";
            default: return "";
        }
    }
}

   /* public static void frame(List<?> ws){
        ArrayList<String[]> tokensTOT = new ArrayList<>();
        String res = "";
        int i = 0, j = 0;
        for (Object w : ws) {
            tokensTOT.add(ws.get(i).toString().split("\n"));
            i++;
        }

        String field = "═";
        String topLeft = "╔";
        String topRight = "╗";
        String bottomLeft = "╚";
        String bottomRight = "╝";
        String field_ = "║";


        int length = tokensTOT.size();
        int heigth = tokensTOT.get(0).length;

        res = res.concat(topLeft);
        for(int a = 0; a < length; a++){
            res = res.concat(field);
        }

        res = res.concat(topRight);
        res = res.concat("\n");

        for(int b = 0; b < heigth; b++){
            res = res.concat(field_);
            for(int a = 0; a < length; a++){
                res = res.concat(" ");
            }
            if(b%2 == 0){
                res = res.concat("\n");
            }
        }

        System.out.println(res);


    }*/



