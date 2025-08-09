package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BlackTimeoutWinController implements ITimeoutWinController {

    @FXML
    private Label timeRemainingLabel;

    // The FXML loader requires a no-argument constructor
    public BlackTimeoutWinController() {
    }

    @Override
    public void setRemainingTime(int timeInSeconds) {
        Platform.runLater(() -> {
            if (timeRemainingLabel != null) {
                int minutes = timeInSeconds / 60;
                int seconds = timeInSeconds % 60;
                timeRemainingLabel.setText("Your Remaining Time: " + String.format("%02d:%02d", minutes, seconds));
            }
        });
    }

    @FXML
    void handleGoHome(ActionEvent event) {
        loadScene("/HomePage.fxml", event);
    }

    @FXML
    void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}