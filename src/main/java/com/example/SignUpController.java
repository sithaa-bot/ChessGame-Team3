package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SignUpController {
    @FXML
    private Button CancelButton;

    @FXML
    private TextField EmailChess;

    @FXML
    private PasswordField PasswordChess;

    @FXML
    private Button SignUpChess;

    @FXML
    private TextField UsernameChess;

    @FXML
    private PasswordField VerifyPasswordChess;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void SignUpButtonOnAction(ActionEvent event) throws Exception {
        if (UsernameChess.getText().isBlank() == false && PasswordChess.getText().isBlank() == false
                && EmailChess.getText().isBlank() == false && VerifyPasswordChess.getText().isBlank() == false) {
            verifySignUp(event);
        } else {
            showAlert("Error", "Please fill in all fields.");
        }
    }

    // cancel button
    @FXML
    void SignUpToLogin(ActionEvent event) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void ToLogin(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/DualMode.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    void verifySignUp(ActionEvent event) throws Exception {
        Database connect = new Database();
        Connection connectToDB = connect.getConnection();

        String signUpUsername = UsernameChess.getText();
        String signUpEmail = EmailChess.getText();
        String signUpPassword = PasswordChess.getText();
        String verifyPassword = VerifyPasswordChess.getText();

        if (!signUpEmail.endsWith("@gmail.com")) {
            showAlert("Error", "Email must be a valid Gmail address!");
            return;
        }
        String query = "INSERT INTO useraccount (name, email, password) VALUE ('" + signUpUsername + "','" + signUpEmail
                + "','" + signUpPassword + "')";
        if (!signUpPassword.equals(verifyPassword)) {
            showAlert("Error", "Fail to verify your Password");
        }
        try {
            Statement statement = connectToDB.createStatement();
            int rowInput = statement.executeUpdate(query);
            if (rowInput > 0) {
                showAlert("Success", "Account Created Successfully");
                ToLogin(event);
            } else {
                showAlert("Fail", "Unable to Create Account!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
