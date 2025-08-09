package com.example;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class RulePageController {

    @FXML
    private void GoBackHomePage(ActionEvent event) {
        switchToHome(event);
    }

    private void switchToHome(ActionEvent event) {
        try {
            // Load HomePage FXML
            Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
            Scene scene = new Scene(root);

            // Apply CSS stylesheet
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());

            // Get current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene and make it full screen
            stage.setScene(scene);
            stage.setFullScreen(true); // Full screen mode
            stage.show();

            // Add fade-
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
