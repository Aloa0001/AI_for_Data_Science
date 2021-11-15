package com.example.ai_for_data_science.players.algorithms;


import com.example.ai_for_data_science.Algorithm;

public class RandomMinimax implements Algorithm {

    RandomMove randomMove;
    Minimax minimax;

    int randomCount;
    boolean collectData;


    public RandomMinimax(boolean isPlayerOne, int randomCount, boolean collectData) {
        randomMove = new RandomMove();
        minimax = new Minimax(isPlayerOne, collectData);
        this.randomCount = randomCount;
    }

    @Override
    public int returnMove(int[] gameBoard) {
        if (randomCount-- > 0) {
            return randomMove.returnMove(gameBoard);
        }
        else {
            return minimax.returnMove(gameBoard);
        }
    }

    @Override
    public void printResults() {

    }
}
