

package com.example;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import java.util.List;

public abstract class Piece implements Moveable {
    protected final String color;
    protected final String imagePath;
    protected int row;
    protected int col;

    public Piece(int row, int col, String color, String imagePath) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.imagePath = imagePath;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public String getFullImagePath() { return color + imagePath; }
    public String getColor() { return color; }
    public int getRow() { return row; }
    public int getColumn() { return col; }
    public abstract int getValue();

    @Override
    public abstract List<Point2D> getValidMoves(GridPane board, Pieces pieces);
}