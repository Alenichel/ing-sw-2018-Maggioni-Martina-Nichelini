package it.polimi.se2018.strategy.objective;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ScorePointStrategy;
import it.polimi.se2018.model.WindowCell;
import it.polimi.se2018.model.WindowPatternCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements Color Diagonals objective card which gives you points every time you have
 * diagonals of dice of the same color.
 */
public class ColorDiagonals implements ScorePointStrategy, Serializable {

    /**
     * This method checks if two cells, a and b, contain a die of the same color
     * @param a first cell
     * @param b second cell
     * @return true if the condition in respected
     */
    private boolean compareCellsColor(WindowCell a, WindowCell b) {
        if (a == null || b == null) return false;
        if (a.getAssignedDice() == null || b.getAssignedDice() == null) return false;
        else {
            if (a.getAssignedDice().getColor().equals(b.getAssignedDice().getColor())) return true;
            else return false;
        }


    }

    /**
     * This method first searches for adjacent cells containing dice of the same color. If there are not it
     * returns the depth of the "tree"
     */
    private int exploreDiagonal(int cellScore, WindowCell[][] grid, WindowCell cell) {
        int sum = 0;
        ArrayList<WindowCell> equalsAdiacent = new ArrayList<>();

        for (WindowCell wc : cell.getDiagonalCells()) {
            if (compareCellsColor(wc, cell)) equalsAdiacent.add(wc);
        }

        if (equalsAdiacent.size() == 0) return cellScore;
        else {
            for (WindowCell wc : equalsAdiacent) {
                wc.getDiagonalCells().remove(cell);
                sum += exploreDiagonal(cellScore + 1, grid, wc);
            }
        }
        return sum;
    }

    /**
     * This methods calculates the score depending on the number of diagonals containing dice of the same color
     * @param windowPatternCard window pattern card under consideration
     * @return points scored with this card
     */
    @Override
    public int scorePoint(WindowPatternCard windowPatternCard) {

        WindowCell[][] grid = windowPatternCard.getGrid();
        Set<WindowCell> diagonals = new HashSet<>();

        for (WindowCell[] line : grid)
            for (WindowCell cell : line) {
                for (WindowCell diagonalCell : cell.getDiagonalCells()) {
                    Dice diagonalDie = diagonalCell.getAssignedDice();
                    Dice cellDie = cell.getAssignedDice();
                    if ( diagonalDie == null || cellDie == null) continue;
                    if (diagonalDie.getDiceColor().equals(cellDie.getDiceColor())) {
                        diagonals.add(cell);
                        diagonals.add(diagonalCell);
                    }
                }
            }

        return diagonals.size();
    }
}
