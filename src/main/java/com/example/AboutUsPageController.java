package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;

public class AboutUsPageController {

    @FXML
    private Hyperlink mylink;

    @FXML
    private Hyperlink mylink2;

    @FXML
    private void gobackhomepage(ActionEvent event) {
        switchToHome(event);
    }

    private void switchToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goYT(ActionEvent event) {
        openLink("https://huothsithaportfolio.netlify.app/");
    }

    @FXML
    private void goNH(ActionEvent event) {
        openLink("https://chinhongnyheng.netlify.app/");
    }

    private void openLink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
