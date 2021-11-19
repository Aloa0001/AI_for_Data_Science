package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;
import com.example.ai_for_data_science.Connect4;
import com.example.ai_for_data_science.DataSet;


public class BayesianClassifier implements Algorithm {

    DataSet dataSet;
    boolean isPlayerOne;


    public BayesianClassifier(DataSet dataSet, boolean isPlayerOne) {
        this.dataSet = dataSet;
        this.isPlayerOne = isPlayerOne;
    }


    @Override
    public int returnMove(int[] gameBoard) {

        float highestProbability = 0;
        int bestMoveCol = 0;

        System.out.print("\nMove probabilities: [ ");
        for (int m : Connect4.getAvailableMoves(gameBoard)) {
            int[] nextGameBoard = Connect4.nextGameBoard(gameBoard, m, isPlayerOne);
            float probability = predictWinProbability(nextGameBoard);

            System.out.print(probability + ", ");

            if (probability > highestProbability) {
                highestProbability = probability;
                bestMoveCol = m;
            }
        }
        System.out.print("]  -  Probability chosen: " + highestProbability + ", moveCol: (" + bestMoveCol + ")");

        return bestMoveCol;
    }

    @Override
    public void printResults() {
        System.out.println("Bayesian Classifier - Results:");
        System.out.println("\n  Accuracy: " + calculateAccuracy());
        //System.out.println("  R²: " + Math.round(rSquared() * 100000.0f) / 1000.0f + "%");
    }


    /**
     * Predicts the probability that this gameBoard results in a win
     */
    private float predictWinProbability(int[] gameBoard) {
        return predict(gameBoard)[0];
    }

    /**
     * Predicts the probability that this gameBoards results in a win, lose, tie, respectively
     */
    private float[] predict(int[] gameBoard) {
        int wins = 0, losses = 0, ties = 0;
        int totalWins = dataSet.winning_gameBoards.size();
        int totalLosses = dataSet.losing_gameBoards.size();
        int totalTies = dataSet.tie_gameBoards.size();

        for (int i = 0; i < dataSet.results.length; i++) {
            switch (dataSet.results[i]) {
                case 1 -> ++wins;
                case -1 -> ++losses;
                case 0 -> ++ties;
            }
        }

        int[] categoryWinCounts = new int[gameBoard.length];
        int[] categoryLoseCounts = new int[gameBoard.length];
        int[] categoryTieCounts = new int[gameBoard.length];

        for (int i = 0; i < dataSet.winning_gameBoards.size(); i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (dataSet.winning_gameBoards.get(i)[j] == gameBoard[j])
                    ++categoryWinCounts[j];
            }
        }
        for (int i = 0; i < dataSet.losing_gameBoards.size(); i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (dataSet.losing_gameBoards.get(i)[j] == gameBoard[j])
                    ++categoryLoseCounts[j];
            }
        }
        for (int i = 0; i < dataSet.tie_gameBoards.size(); i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (dataSet.tie_gameBoards.get(i)[j] == gameBoard[j])
                    ++categoryTieCounts[j];
            }
        }

        float likelyhoodWin = (float)wins / totalWins;
        float likelyhoodLose = (float)losses / totalLosses;
        float likelyhoodTie = (float)ties / totalTies;

        for (int i = 0; i < categoryWinCounts.length; i++) {
            likelyhoodWin *= (float)(categoryWinCounts[i] + 1) / totalWins;   // +1 acts as a bias
        }
        for (int i = 0; i < categoryLoseCounts.length; i++) {
            likelyhoodLose *= (float)(categoryLoseCounts[i] + 1) / totalLosses;   // +1 acts as a bias
        }
        for (int i = 0; i < categoryTieCounts.length; i++) {
            likelyhoodTie *= (float)(categoryTieCounts[i] + 1) / totalTies;   // +1 acts as a bias
        }

        float likelyhoodEvidence = likelyhoodWin + likelyhoodLose + likelyhoodTie;
        return new float[] { likelyhoodWin / likelyhoodEvidence, likelyhoodLose / likelyhoodEvidence, likelyhoodTie / likelyhoodEvidence };   //The probabilities that this gameBoard results in a win[0], lose[1], tie[2]
    }


    private float calculateAccuracy() {

        int correctClassifications = 0;
        int totalClassifications = dataSet.testData_results.length / 20;

        System.out.print("  Evaluating " + totalClassifications + " entries...  ");
        for (int i = 0; i < totalClassifications; i++) {
            if (i % 100 == 0)
                System.out.print((double)Math.round((float)i/totalClassifications*10000)/100 + "%  ");

            float[] predictions = predict(dataSet.testData_gameBoards[i]);

            float highestProbability = predictions[0];
            int bestIndex = 0;
            if (predictions[1] > highestProbability)  {
                highestProbability = predictions[1];
                bestIndex = 1;
            }
            if (predictions[2] > highestProbability)  {
                bestIndex = 2;
            }

            int predictedClassification = 0;
            switch (bestIndex){
                case 0 -> predictedClassification = 1;
                case 1 -> predictedClassification = -1;
                case 2 -> predictedClassification = 0;
            }

            if (predictedClassification == dataSet.testData_results[i]) {
                correctClassifications++;
            }
        }

        return (float)correctClassifications / totalClassifications;
    }

}
