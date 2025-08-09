package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ChessController {

    @FXML private GridPane chessBoard;
    @FXML private Label turnLabel;
    @FXML private Label whiteTimerLabel;
    @FXML private Label blackTimerLabel;
    @FXML private VBox timeSelectionBox;
    @FXML private Button startButton;
    @FXML private Label selectedTimeLabel;
    @FXML private HBox opponentBox;
    @FXML private HBox playerBox;
    @FXML private Label whiteScoreLabel;
    @FXML private Label blackScoreLabel;
    @FXML private VBox promotionBox;
    @FXML private ImageView queenPromotion;
    @FXML private ImageView rookPromotion;
    @FXML private ImageView bishopPromotion;
    @FXML private ImageView knightPromotion;

    private Timeline timeline;
    private int whiteTimeSeconds;
    private int blackTimeSeconds;
    private int selectedTimeInSeconds = -1;
    
    private static int whiteScore = 0;
    private static int blackScore = 0;
    
    private static Label whiteScoreLabelStatic;
    private static Label blackScoreLabelStatic;

    private static ChessController instance;
    private ImageView pawnToPromoteView;

    public ChessController() {
        instance = this;
    }

    public static ChessController getInstance() {
        return instance;
    }

    @FXML
    private void select5Minutes(ActionEvent event) {
        prepareGame(300, "5 Minutes");
    }

    @FXML
    private void select10Minutes(ActionEvent event) {
        prepareGame(600, "10 Minutes");
    }

    @FXML
    private void select15Minutes(ActionEvent event) {
        prepareGame(900, "15 Minutes");
    }
    
    private void prepareGame(int timeInSeconds, String timeText) {
        this.selectedTimeInSeconds = timeInSeconds;
        selectedTimeLabel.setText("Selected: " + timeText);
        selectedTimeLabel.setVisible(true);
        selectedTimeLabel.setManaged(true);
        startButton.setVisible(true);
        startButton.setManaged(true);
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        if (selectedTimeInSeconds > 0) {
            this.whiteTimeSeconds = selectedTimeInSeconds;
            this.blackTimeSeconds = selectedTimeInSeconds;

            updateTimerLabel(whiteTimerLabel, whiteTimeSeconds);
            updateTimerLabel(blackTimerLabel, blackTimeSeconds);

            timeSelectionBox.setVisible(false);
            timeSelectionBox.setManaged(false);
            
            opponentBox.setVisible(true);
            opponentBox.setManaged(true);
            playerBox.setVisible(true);
            playerBox.setManaged(true);
            
            turnLabel.setVisible(true);
            turnLabel.setManaged(true);

            Pieces.enablePieceInteractions(chessBoard);

            startTimer();
        }
    }
    
    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (whiteTimeSeconds > 0 && blackTimeSeconds > 0) {
                if (Pieces.isWhiteTurn()) {
                    whiteTimeSeconds--;
                    updateTimerLabel(whiteTimerLabel, whiteTimeSeconds);
                } else {
                    blackTimeSeconds--;
                    updateTimerLabel(blackTimerLabel, blackTimeSeconds);
                }
            }

            if (blackTimeSeconds <= 0) {
                timeline.stop();
                Platform.runLater(() -> loadScene("/WhiteWinTime.fxml", chessBoard, whiteTimeSeconds));
            } 
            else if (whiteTimeSeconds <= 0) {
                timeline.stop();
                Platform.runLater(() -> loadScene("/BlackWinTime.fxml", chessBoard, blackTimeSeconds));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void updateTimerLabel(Label label, int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        label.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void loadScene(String fxmlPath, Node node, int remainingTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
    
            ITimeoutWinController controller = loader.getController();
            controller.setRemainingTime(remainingTime);
    
            Scene scene = new Scene(root);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void GoBackHomePage(ActionEvent event) {
        try {
            if (timeline != null) {
                timeline.stop();
            }
            Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        final int SIZE = 8;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = new StackPane();
                String color = ((row + col) % 2 == 0) ? "#FFFFFF" : "#8B4513";
                cell.setStyle("-fx-background-color: " + color + ";");
                cell.setPrefSize(90, 90);
                chessBoard.add(cell, col, row);
            }
        }
        
        whiteScoreLabelStatic = whiteScoreLabel;
        blackScoreLabelStatic = blackScoreLabel;
        updateScoreLabels();

        for (int col = 0; col < SIZE; col++) {
            Pieces.addPieces(chessBoard, new Pawn(1, col, "black"));
            Pieces.addPieces(chessBoard, new Pawn(6, col, "white"));
        }
        Pieces.addPieces(chessBoard, new Rook(0, 0, "black"));
        Pieces.addPieces(chessBoard, new Rook(0, 7, "black"));
        Pieces.addPieces(chessBoard, new Rook(7, 0, "white"));
        Pieces.addPieces(chessBoard, new Rook(7, 7, "white"));
        Pieces.addPieces(chessBoard, new Knight(0, 1, "black"));
        Pieces.addPieces(chessBoard, new Knight(0, 6, "black"));
        Pieces.addPieces(chessBoard, new Knight(7, 1, "white"));
        Pieces.addPieces(chessBoard, new Knight(7, 6, "white"));
        Pieces.addPieces(chessBoard, new Bishop(0, 2, "black"));
        Pieces.addPieces(chessBoard, new Bishop(0, 5, "black"));
        Pieces.addPieces(chessBoard, new Bishop(7, 2, "white"));
        Pieces.addPieces(chessBoard, new Bishop(7, 5, "white"));
        Pieces.addPieces(chessBoard, new Queen(0, 3, "black"));
        Pieces.addPieces(chessBoard, new Queen(7, 3, "white"));
        Pieces.addPieces(chessBoard, new King(0, 4, "black"));
        Pieces.addPieces(chessBoard, new King(7, 4, "white"));

        Pieces.initializeTurn(turnLabel);
    }
    
    public static void addScore(String color, int value) {
        if (color.equals("white")) {
            whiteScore += value;
        } else {
            blackScore += value;
        }
        updateScoreLabels();
    }

    private static void updateScoreLabels() {
        if (whiteScoreLabelStatic != null) {
            whiteScoreLabelStatic.setText("Score " + whiteScore);
        }
        if (blackScoreLabelStatic != null) {
            blackScoreLabelStatic.setText("Score " + blackScore);
        }
    }

    public void setInitialTime(int timeInSeconds) {
        this.selectedTimeInSeconds = timeInSeconds;
        this.whiteTimeSeconds = timeInSeconds;
        this.blackTimeSeconds = timeInSeconds;
    }

    public void showPromotionChoice(ImageView pawnView) {
        this.pawnToPromoteView = pawnView;
        Piece pawn = (Piece) pawnView.getUserData();
        String color = pawn.getColor();

        queenPromotion.setImage(new Image(getClass().getResourceAsStream("/img/" + color + "Queen.png")));
        rookPromotion.setImage(new Image(getClass().getResourceAsStream("/img/" + color + "Rook.png")));
        bishopPromotion.setImage(new Image(getClass().getResourceAsStream("/img/" + color + "Bishop.png")));
        knightPromotion.setImage(new Image(getClass().getResourceAsStream("/img/" + color + "Knight.png")));

        promotionBox.setVisible(true);
        promotionBox.setManaged(true);
    }

    private void promoteTo(Piece newPiece) {
        StackPane cell = (StackPane) pawnToPromoteView.getParent();
        Pieces.replacePawnWithPromotedPiece(cell, newPiece);

        promotionBox.setVisible(false);
        promotionBox.setManaged(false);
        pawnToPromoteView = null;
        Pieces.switchTurn();
    }

    @FXML
    private void promoteToQueen(MouseEvent event) {
        Piece pawn = (Piece) pawnToPromoteView.getUserData();
        promoteTo(new Queen(pawn.getRow(), pawn.getColumn(), pawn.getColor()));
    }

    @FXML
    private void promoteToRook(MouseEvent event) {
        Piece pawn = (Piece) pawnToPromoteView.getUserData();
        promoteTo(new Rook(pawn.getRow(), pawn.getColumn(), pawn.getColor()));
    }

    @FXML
    private void promoteToBishop(MouseEvent event) {
        Piece pawn = (Piece) pawnToPromoteView.getUserData();
        promoteTo(new Bishop(pawn.getRow(), pawn.getColumn(), pawn.getColor()));
    }

    @FXML
    private void promoteToKnight(MouseEvent event) {
        Piece pawn = (Piece) pawnToPromoteView.getUserData();
        promoteTo(new Knight(pawn.getRow(), pawn.getColumn(), pawn.getColor()));
    }
}