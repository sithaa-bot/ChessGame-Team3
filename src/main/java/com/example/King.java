// src/main/java/com/example/King.java

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(int row, int col, String color) {
        super(row, col, color, "King");
    }

    @Override
    public int getValue() {
        return 0; 
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        List<Point2D> moves = new ArrayList<>();
        int[][] offsets = {
                { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 },
                { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
        };
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