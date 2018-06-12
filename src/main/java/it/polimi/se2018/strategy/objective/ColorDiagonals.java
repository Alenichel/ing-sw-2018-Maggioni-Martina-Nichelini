package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;
import java.util.ArrayList;

public class ColorDiagonals implements ScorePointStrategy, Serializable{

    private boolean compareCellsColor (WindowCell a, WindowCell b) {
        if (a == null || b == null) return false;
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return false;
        else {
            if (a.getAssignedDice().getColor().equals(b.getAssignedDice().getColor())) return true;
            else return false;
        }


    }

    private int exploreDiagonal(int cellScore, WindowCell[][] grid, WindowCell cell){
        int sum = 0;
        ArrayList<WindowCell> equalsAdiacent = new ArrayList<>();

        //Controllo se ci sono delle celle adiacenti con un dado dello stesso colore di quello della cella passata
        for (WindowCell wc : cell.getDiagonalCells()){
            if (compareCellsColor(wc, cell)) equalsAdiacent.add(wc);
        }

        //se non ce ne sono, ritorno la profondità "dell'albero"
        if (equalsAdiacent.size() == 0) return cellScore;
        else {
            for (WindowCell wc : equalsAdiacent){
                wc.getDiagonalCells().remove(cell);
                sum += exploreDiagonal(cellScore+1, grid, wc);
            }
        }
        return sum;
    }

    @Override
    public int scorePoint(WindowPatternCard windowPatternCard){
        WindowCell[][] grid = windowPatternCard.getGrid();
        int scoreCounter = 0;

        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++){
                    scoreCounter+=exploreDiagonal(0, grid, grid[i][j]);
            }
            return scoreCounter;
    }

    /*
    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {
        int scoreCounter = 0;
        WindowCell[][] grid = windowPatternCard.getGrid();

        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                if (compareCellsColor(grid[i][j], grid[i-1][j-1]) ||
                    compareCellsColor(grid[i][j], grid [i+1][j+1]) ||
                    compareCellsColor(grid[i][j], grid [i+1][j-1]) ||
                    compareCellsColor(grid[i][j], grid [i-1][j+1])) {scoreCounter += 1;}

        return scoreCounter;
    }*/
}
