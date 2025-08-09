// src/main/java/com/example/Pawn.java

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(int row, int col, String color) {
        super(row, col, color, "Pawn");
    }

    @Override
    public int getValue() {
        return 1;
    }

    public boolean isPromotionZone(int row) {
        if (getColor().equals("white")) {
            return row == 0;
        } else {
            return row == 7;
        }
    }

    @Override
    public List<Point2D> getValidMoves(GridPane board, Pieces pieces) {
        List<Point2D> moves = new ArrayList<>();
        int startRow = getRow();
        int startCol = getColumn();
        int direction = this.getColor().equals("white") ? -1 : 1;
        int startRank = this.getColor().equals("white") ? 6 : 1;
        int oneStepRow = startRow + direction;
        if (oneStepRow >= 0 && oneStepRow < 8) {
            if (pieces.getPieceAt(board, oneStepRow, startCol) == null) {
                moves.add(new Point2D(oneStepRow, startCol));
                int twoStepsRow = startRow + 2 * direction;
                if (startRow == startRank && pieces.getPieceAt(board, twoStepsRow, startCol) == null) {
                    moves.add(new Point2D(twoStepsRow, startCol));
                }
            }
        }
        for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
            int captureCol = startCol + colOffset;
            if (oneStepRow >= 0 && oneStepRow < 8 && captureCol >= 0 && captureCol < 8) {
                Piece targetPiece = pieces.getPieceAt(board, oneStepRow, captureCol);
                if (targetPiece != null && !targetPiece.getColor().equals(this.getColor())) {
                    moves.add(new Point2D(oneStepRow, captureCol));
                }
            }
        }
        return moves;
    }
}