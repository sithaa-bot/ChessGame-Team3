

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(int row, int col, String color) {
        super(row, col, color, "Knight");
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        List<Point2D> moves = new ArrayList<>();
        int[][] offsets = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 } };
        int startRow = getRow();
        int startCol = getColumn();
        for (int[] offset : offsets) {
            int newRow = startRow + offset[0];
            int newCol = startCol + offset[1];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Piece targetPiece = pieces.getPieceAt(board, newRow, newCol);
                if (targetPiece == null || !targetPiece.getColor().equals(this.getColor())) {
                    moves.add(new Point2D(newRow, newCol));
                }
            }
        }
        return moves;
    }
}