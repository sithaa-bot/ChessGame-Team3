

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(int row, int col, String color) {
        super(row, col, color, "Bishop");
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        return pieces.getSlidingPieceMoves(board, this, directions);
    }
}