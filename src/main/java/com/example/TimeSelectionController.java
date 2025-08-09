package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TimeSelectionController {

    @FXML
    void select5Minutes(ActionEvent event) {
        loadPlayScene(event, 300); // 300 seconds = 5 minutes
    }

    @FXML
    void select10Minutes(ActionEvent event) {
        loadPlayScene(event, 600); // 600 seconds = 10 minutes
    }

    @FXML
    void select15Minutes(ActionEvent event) {
        loadPlayScene(event, 900); // 900 seconds = 15 minutes
    }

    private void loadPlayScene(ActionEvent event, int timeInSeconds) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlayPage.fxml"));
            Parent root = loader.load();

            // Get the controller of the PlayPage and pass the time to it
            ChessController chessController = loader.getController();
            chessController.setInitialTime(timeInSeconds);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}