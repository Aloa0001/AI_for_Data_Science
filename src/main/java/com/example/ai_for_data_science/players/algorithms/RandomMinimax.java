package com.example.ai_for_data_science.players.algorithms;


import com.example.ai_for_data_science.Algorithm;

public class RandomMinimax implements Algorithm {

    RandomMove randomMove;
    Minimax minimax;

    int randomCount;


    public RandomMinimax(boolean isPlayerOne, int randomCount) {
        randomMove = new RandomMove();
        minimax = new Minimax(isPlayerOne);
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