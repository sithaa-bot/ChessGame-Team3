package com.example;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pieces {
    private static boolean isWhiteTurn = true;
    private static Label turnLabel;
    private static ImageView activePieceView = null;
    private static final List<Circle> moveDots = new ArrayList<>();
    private static MediaPlayer mediaPlayer;

    public static boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public static void initializeTurn(Label label) {
        turnLabel = label;
        updateTurnLabel();
    }

    private static void updateTurnLabel() {
        if (turnLabel != null) {
            turnLabel.setText(isWhiteTurn ? "White's Turn" : "Black's Turn");
        }
    }

    public static void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
        updateTurnLabel();
    }

    private static boolean isPieceOwnedByCurrentPlayer(Piece piece) {
        return (isWhiteTurn && piece.getColor().equals("white")) ||
               (!isWhiteTurn && piece.getColor().equals("black"));
    }

    public static void addPieces(GridPane chessBoard, Piece piece) {
        String imageResourcePath = "/img/" + piece.getFullImagePath() + ".png";
        Image img = new Image(Pieces.class.getResourceAsStream(imageResourcePath));
        ImageView pieceImageView = new ImageView(img);
        pieceImageView.setFitHeight(90);
        pieceImageView.setFitWidth(90);
        pieceImageView.setUserData(piece);
        for (Node node : chessBoard.getChildren()) {
            if (node instanceof StackPane) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                if (r != null && c != null && r == piece.getRow() && c == piece.getColumn()) {
                    ((StackPane) node).getChildren().add(pieceImageView);
                    break;
                }
            }
        }
    }

    public static void enablePieceInteractions(GridPane chessBoard) {
        for (Node node : chessBoard.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                setupClickAndDragHandlers(cell, chessBoard);
            }
        }
    }

    private static void setupClickAndDragHandlers(StackPane cell, GridPane chessBoard) {
        cell.setOnMouseClicked(event -> {
            ImageView clickedPieceView = getPieceViewAt(cell);
            Piece clickedPiece = (clickedPieceView != null) ? (Piece) clickedPieceView.getUserData() : null;

            if (activePieceView == null) {
                if (clickedPiece != null && isPieceOwnedByCurrentPlayer(clickedPiece)) {
                    selectPiece(clickedPieceView, chessBoard);
                }
            } else {
                if (clickedPieceView == activePieceView) {
                    deselectPiece();
                } else if (clickedPiece != null && isPieceOwnedByCurrentPlayer(clickedPiece)) {
                    deselectPiece();
                    selectPiece(clickedPieceView, chessBoard);
                } else {
                    if (isValidMove(chessBoard, GridPane.getRowIndex(cell), GridPane.getColumnIndex(cell))) {
                        movePieceToCell(activePieceView, cell);
                    } else {
                        deselectPiece();
                    }
                }
            }
            event.consume();
        });

        cell.setOnDragDetected(event -> {
            ImageView pieceToDrag = getPieceViewAt(cell);
            if (pieceToDrag != null && isPieceOwnedByCurrentPlayer((Piece) pieceToDrag.getUserData())) {
                selectPiece(pieceToDrag, chessBoard);
                ClipboardContent content = new ClipboardContent();
                content.putImage(activePieceView.getImage());
                activePieceView.startDragAndDrop(TransferMode.MOVE).setContent(content);
                activePieceView.setVisible(false);
            }
            event.consume();
        });

        cell.setOnDragOver(event -> {
            if (activePieceView != null && event.getGestureSource() != cell) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            boolean success = false;
            if (activePieceView != null) {
                int targetRow = GridPane.getRowIndex(cell);
                int targetCol = GridPane.getColumnIndex(cell);
                if (isValidMove(chessBoard, targetRow, targetCol)) {
                    movePieceToCell(activePieceView, cell);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        cell.setOnDragDone(event -> {
            if (activePieceView != null) {
                activePieceView.setVisible(true);
            }
            deselectPiece();
            event.consume();
        });
    }

    private static void selectPiece(ImageView pieceView, GridPane chessBoard) {
        activePieceView = pieceView;
        showValidMoveDots(chessBoard);
    }

    private static void deselectPiece() {
        clearMoveDots();
        activePieceView = null;
    }

    private static void movePieceToCell(ImageView pieceImageView, StackPane newCell) {
        int newRow = GridPane.getRowIndex(newCell);
        int newCol = GridPane.getColumnIndex(newCell);
        Piece piece = (Piece) pieceImageView.getUserData();
        piece.setPosition(newRow, newCol);

        StackPane oldCell = (StackPane) pieceImageView.getParent();
        if (oldCell != null) {
            oldCell.getChildren().remove(pieceImageView);
        }
        
        boolean isCapture = false;
        ImageView capturedPieceView = getPieceViewAt(newCell);
        if (capturedPieceView != null) {
            isCapture = true;
            Piece capturedPiece = (Piece) capturedPieceView.getUserData();
            ChessController.addScore(piece.getColor(), capturedPiece.getValue());
        }

        newCell.getChildren().removeIf(child -> child instanceof ImageView);
        
        newCell.getChildren().add(pieceImageView);

        if (isCapture) {
            playSound("Capture.wav");
        } else {
            playSound("Move.wav");
        }

        if (piece instanceof Pawn && ((Pawn) piece).isPromotionZone(newRow)) {
            ChessController.getInstance().showPromotionChoice(pieceImageView);
        } else {
            switchTurn();
        }
        deselectPiece();
    }

    public static void replacePawnWithPromotedPiece(StackPane cell, Piece newPiece) {
        cell.getChildren().removeIf(node -> node instanceof ImageView);
        addPieces((GridPane) cell.getParent(), newPiece);
    }

    private static void playSound(String soundFile) {
        try {
            String resourcePath = "/sounds/" + soundFile;
            URL resource = Pieces.class.getResource(resourcePath);

            if (resource == null) {
                System.err.println("Sound not found: " );
                return;
            }

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media sound = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.setOnError(() -> {
                System.err.println("MediaPlayer Error: " + mediaPlayer.getError());
                if (mediaPlayer.getError() != null) {
                    mediaPlayer.getError().printStackTrace();
                }
            });


            System.out.println("Playing sound: " + soundFile);
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Exception in playSound method:");
            e.printStackTrace();
        }
    }

    private static void showValidMoveDots(GridPane chessBoard) {
        if (activePieceView == null) return;
        Piece piece = (Piece) activePieceView.getUserData();
        List<Point2D> validMoves = piece.getValidMoves(chessBoard, new Pieces());

        for (Point2D move : validMoves) {
            StackPane targetCell = getCellAt(chessBoard, (int) move.getX(), (int) move.getY());
            if (targetCell != null) {
                Circle dot = new Circle(10, Color.web("#808080", 0.6));
                dot.setMouseTransparent(true);
                targetCell.getChildren().add(dot);
                moveDots.add(dot);
            }
        }
    }

    private static void clearMoveDots() {
        for (Circle dot : moveDots) {
            if (dot.getParent() instanceof StackPane) {
                ((StackPane) dot.getParent()).getChildren().remove(dot);
            }
        }
        moveDots.clear();
    }

    private static boolean isValidMove(GridPane board, int row, int col) {
        if (activePieceView == null) return false;
        Piece piece = (Piece) activePieceView.getUserData();
        List<Point2D> validMoves = piece.getValidMoves(board, new Pieces());
        for (Point2D move : validMoves) {
            if (move.getX() == row && move.getY() == col) {
                return true;
            }
        }
        return false;
    }

    private static ImageView getPieceViewAt(StackPane cell) {
        for (Node child : cell.getChildren()) {
            if (child instanceof ImageView) {
                return (ImageView) child;
            }
        }
        return null;
    }

    private static StackPane getCellAt(GridPane board, int row, int col) {
        for (Node node : board.getChildren()) {
            if (node instanceof StackPane) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                if (r != null && c != null && r == row && c == col) {
                    return (StackPane) node;
                }
            }
        }
        return null;
    }

    public List<Point2D> getSlidingPieceMoves(GridPane board, Piece piece, int[][] directions) {
        List<Point2D> moves = new ArrayList<>();
        int startRow = piece.getRow();
        int startCol = piece.getColumn();
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = startRow + i * dir[0];
                int newCol = startCol + i * dir[1];
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    Piece pieceAt = getPieceAt(board, newRow, newCol);
                    if (pieceAt == null) {
                        moves.add(new Point2D(newRow, newCol));
                    } else {
                        if (!pieceAt.getColor().equals(piece.getColor())) {
                            moves.add(new Point2D(newRow, newCol));
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public Piece getPieceAt(GridPane board, int row, int col) {
        StackPane cell = getCellAt(board, row, col);
        if (cell != null) {
            ImageView pieceView = getPieceViewAt(cell);
            if (pieceView != null) {
                return (Piece) pieceView.getUserData();
            }
        }
        return null;
    }
}