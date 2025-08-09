

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(int row, int col, String color) {
        super(row, col, color, "Queen");
    }

    @Override
    public int getValue() {
        return 9;
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        List<Point2D> moves = new ArrayList<>();
        int[][] directions = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };
        moves.addAll(pieces.getSlidingPieceMoves(board, this, directions));
        return moves;
    }
}