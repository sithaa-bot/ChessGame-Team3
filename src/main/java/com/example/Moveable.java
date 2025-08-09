package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.List;

/**
 * Interface for any object that can move on the chessboard.
 */
public interface Moveable {
    /**
     * Calculates and returns a list of valid moves for the piece.
     * @param board The current chessboard.
     * @param pieces The main game logic controller.
     * @return A list of Point2D coordinates representing valid moves.
     */
    List<Point2D> getValidMoves(GridPane board, Pieces pieces);
}