package com.example.ai_for_data_science;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Connect4 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Connect4.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1431,897);
        stage.setTitle("Connect 4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}