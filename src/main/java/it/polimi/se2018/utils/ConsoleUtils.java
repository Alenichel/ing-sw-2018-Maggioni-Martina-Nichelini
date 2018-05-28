package it.polimi.se2018.utils;

        import it.polimi.se2018.model.WindowPatternCard;

        import java.util.ArrayList;

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
}


