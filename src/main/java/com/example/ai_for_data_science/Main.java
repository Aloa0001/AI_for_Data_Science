package com.example.ai_for_data_science;


import com.example.ai_for_data_science.players.algorithms.*;

import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] args) {
        run();
    }

    private static void run() {

        DataSet dataSet = new DataSet();
        try {
            dataSet.preProcessing();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataSet.generateTrainTestSets();
        dataSet.generateWinLoseTieSets();


        Algorithm player1 = new BayesianClassifier(dataSet, true);
        Algorithm player2 = new SVM(dataSet);
        //Algorithm player2 = new LinearRegression(0.001f, 10000, 42, false);
        //Algorithm player2 = new Minimax(false, 5);
        //Algorithm player2 = new Human();
        //Algorithm player2 = new RandomMove();


        System.out.println(player1.getClass().getSimpleName() + " vs " + player2.getClass().getSimpleName());

        Connect4 c4 = new Connect4();

        c4.play(player1, player2);

        player1.printResults();
        player2.printResults();
    }
}
