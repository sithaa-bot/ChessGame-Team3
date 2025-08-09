package com.example;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.IOException;

public class HomePageController {

    @FXML
    private Button playButton;

    @FXML
    private Button ruleButton;

    @FXML
    private Button aboutButton;

    @FXML
    private void handlePlay(ActionEvent event) {
        // This now correctly points to the time selection screen
        switchSceneWithFade(event, "/PlayPage.fxml");
    }
    
    @FXML
    private void handleRule(ActionEvent event) {
        switchSceneWithFade(event, "/RulePage.fxml");
    }

    @FXML
    private void handleAboutUs(ActionEvent event) {
        switchSceneWithFade(event, "/AboutUsPage.fxml");
    }

    private void switchSceneWithFade(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm()); // Optional

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Add fade transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            stage.setScene(scene);
            stage.setFullScreen(true); 
            stage.show();

            fadeIn.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}