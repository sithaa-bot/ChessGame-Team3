package com.example;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.*;

public class LoginController {

    @FXML
    private Button CancelChess;

    @FXML
    private Button LoginChess;

    @FXML
    private TextField Namechess;

    @FXML
    private Button SIgnUpChessGame;

    @FXML
    private PasswordField passwordChess;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void LoginToSignUp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
        // event.getSource() -> Node -> getScene -> getWindow() -> cast to Stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/DualMode.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) CancelChess.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) throws Exception {
        if (Namechess.getText().isBlank() == false && passwordChess.getText().isBlank() == false) {
            if (verifyUser()) {
                root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/itc.png")));
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert("Error", "Incorrect UserName and Password! Please try again!!");
            }
        } else {
            showAlert("Error", "No UserInput");
        }
    }

    public boolean verifyUser() {
        Database connect = new Database();
        Connection connectToDB = connect.getConnection();

        String userName = Namechess.getText();
        String password = passwordChess.getText();

        String query = "SELECT COUNT(1) FROM useraccount WHERE name = '" + userName + "'AND password = '" + password
                + "'";
        try {
            Statement statement = connectToDB.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                if (result.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
