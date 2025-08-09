package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    // Stage -> Scene -> Root
    // All Element -> Root
    // Root -> Scene
    // Scene -> Stage
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        primaryStage.setTitle("Chess game");
        Scene scene = new Scene(root, 520, 400);
        scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);
        

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/itc.png")));
        primaryStage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
    }
}
