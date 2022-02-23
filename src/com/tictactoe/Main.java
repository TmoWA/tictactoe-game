package com.tictactoe;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("com/tictactoe/Main.fxml"));
        Parent root = fxmlLoader.load();
        root.getStylesheets().add("tic_tac_toe_style.css");

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}