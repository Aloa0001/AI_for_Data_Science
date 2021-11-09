package com.example.ai_for_data_science;

import com.example.ai_for_data_science.players.algorithms.LinearRegression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Arrays;

public class Dashboard {

    private static Stage stage;

    public void run() {


        float[][] independentFeatures = new float[][] {
                {0.79f, 0.77f},
                {1.28f, 1.77f},
                {1.69f, 1.07f},
                {2.34f, 2.68f},
                {3.26f, 1.82f},
                {4.12f, 3.42f}
        };
        float[] dependentFeatures = new float[] {
                0.81f,
                1.24f,
                2.00f,
                2.50f,
                4.02f,
                4.80f
        };
        float[] weights = new float[] {
                1.0f,
                1.0f
        };

        LinearRegression regression = new LinearRegression(independentFeatures, dependentFeatures, weights,
                0.0f,0.01f, 1000,3);
        regression.train();
        regression.print();



        ObservableList<String> players = FXCollections
                .observableArrayList("Human", "Minimax", "Decision Tree", "BayesianClassifier", "SVM");
        var playerOne = new ChoiceBox();
        ChoiceBox<String> playerTwo = new ChoiceBox<>();

        Arrays.asList(playerOne, playerTwo).forEach(choiceBox -> {
            choiceBox.setItems(players);
            playerOne.setValue("First Player");
            playerTwo.setValue("Second player");
        });
        playerOne.setLayoutX(40);
        playerOne.setLayoutY(20);
        playerTwo.setLayoutX(280);
        playerTwo.setLayoutY(20);

        Button start = new Button("PLAY");
        start.setLayoutX(190);
        start.setLayoutY(60);
        start.setOnAction(e -> {
            String p1 = (String) playerOne.getValue();
            String p2 = playerTwo.getValue();
            startGame(p1, p2);
        });

        Button exit = new Button("EXIT");
        exit.setLayoutX(450);
        exit.setLayoutY(320);
        exit.setOnAction(e -> {
            exitGame();
        });

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(500, 350);
        anchorPane.getChildren().addAll(playerOne, playerTwo, start, exit);
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setTitle("Connect 4 - Dashboard");
        stage.setScene(scene);
        stage.show();

        Dashboard.stage = stage;
    }

    private void exitGame() {
        System.exit(0);
    }

    private void startGame(String firstPlayer, String secondPlayer){
        new Connect4().play(firstPlayer, secondPlayer);
        stage.close();
    }

}
