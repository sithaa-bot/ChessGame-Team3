
package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(int row, int col, String color) {
        super(row, col, color, "Rook");
    }

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        return pieces.getSlidingPieceMoves(board, this, directions);
    }
}