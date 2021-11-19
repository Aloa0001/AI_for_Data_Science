package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;
import com.example.ai_for_data_science.Connect4;
import com.example.ai_for_data_science.DataSet;


public class BayesianClassifier implements Algorithm {

    DataSet dataSet;
    boolean isPlayerOne;


    public BayesianClassifier(DataSet dataSet, boolean isPlayerOne) {
        this.dataSet = dataSet;
    }


    @Override
    public int returnMove(int[] gameBoard) {

        float highestLikelyhood = 0;
        int bestMoveCol = 0;

        for (int m : Connect4.getAvailableMoves(gameBoard)) {
            int[] nextGameBoard = Connect4.nextGameBoard(gameBoard, m, isPlayerOne);
            float likelyhood = classifier(nextGameBoard);

            if (likelyhood > highestLikelyhood) {
                highestLikelyhood = likelyhood;
                bestMoveCol = m;
            }
        }

        return bestMoveCol;
    }

    @Override
    public void printResults() {
    }



    private float classifier(int[] gameBoard) {
        int wins = 0;
        int totalGames = dataSet.results.length;

        for (int i = 0; i < dataSet.results.length; i++) {
            if (dataSet.results[i] == 1){
                ++wins;
            }
        }

        int[] categoryYesCounts = new int[gameBoard.length];

        for (int i = 0; i < dataSet.winning_gameBoards.size(); i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (dataSet.winning_gameBoards.get(i)[j] == gameBoard[j])
                    ++categoryYesCounts[j];
            }
        }
        float likelyhood_win = (float)wins / totalGames;
        for (int i = 0; i < categoryYesCounts.length; i++) {
            likelyhood_win *= (float)(categoryYesCounts[i] + 1) / totalGames;   // +1 acts as a bias
        }

        return likelyhood_win;
    }
}
