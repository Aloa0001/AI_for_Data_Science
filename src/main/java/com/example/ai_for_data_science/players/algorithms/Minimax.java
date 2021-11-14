package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;
import com.example.ai_for_data_science.Connect4;

import java.util.ArrayList;
import java.util.Arrays;

public class Minimax implements Algorithm {
    final int depth = 2; // -1 means infinite depth
    int nodesExamined = 0;
    int branchesPruned = 0;


//        0  1  2  3  4  5  6
//        7  8  9  10 11 12 13
//        14 15 16 17 18 19 20
//        21 22 23 24 25 26 27
//        28 29 30 31 32 33 34
//        35 36 37 38 39 40 41


    @Override
    public int returnMove(int[] gameBoard, boolean isPlayerOne) {
        nodesExamined = 0;
        branchesPruned = 0;

        int bestEval = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col : getAvailableMoves(gameBoard))
        {
            int moveEval = minimax(gameBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            if (moveEval > bestEval) {
                bestMove = col;
                bestEval = moveEval;
            }
        }

        return bestMove;
    }


    private int minimax(int[] gameBoard, int depth, int alpha, int beta, boolean maximizingPlayer){

        ++nodesExamined;

        if (Connect4.gameIsFinished(gameBoard) > -1) // if the game finished => evaluate the position to find the next best move
            return evalGameFinished(gameBoard);
        if (depth == 0)     // if the maximum depth was hit => evaluate the position to find the next best move
            return evalGame(gameBoard);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int col : getAvailableMoves(gameBoard)) {
                int moveEval = minimax(Connect4.nextGameBoard(gameBoard, col, true), depth-1, alpha, beta, false);
                maxEval = Math.max(maxEval, moveEval);

                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha)
                {
                    ++branchesPruned;
                    break;
                }
            }

            return maxEval;
        }
        else
        {
            int minEval = Integer.MAX_VALUE;

            for (int col: getAvailableMoves(gameBoard)) {
                int moveEval = minimax(Connect4.nextGameBoard(gameBoard ,col, false), depth-1, alpha, beta, true);
                minEval = Math.min(minEval, moveEval);

                beta = Math.min(beta, minEval);
                if (beta <= alpha)
                {
                    ++branchesPruned;
                    break;
                }
            }

            return minEval;
        }
    }

    /**
     * @return A list of valid columns where the disc can be placed
     */
    private ArrayList<Integer> getAvailableMoves(int[] gameBoard) {
        ArrayList<Integer> moves = new ArrayList<>();
        for (int col = 0; col < 7; col++) {
            if (gameBoard[col] == 0) moves.add(col);
        }
        return moves;
    }

    /**
     * Evaluates how good a won/tied position is.
     * The best position is the one where player 1 won, and the worst is when player 2 won
     */
    private int evalGameFinished(int[] gameBoard) {

        int eval = Connect4.gameIsFinished(gameBoard);
        if (eval == 2) eval = -1; //assigns negative score to the position where player 2 won
        return eval;
    }

    /**
     * Evaluates how good a not won/tied position is.
     * Checks each combination of 4 tiles in a row, and sums the number of player1 discs - player2 discs
     * This means the evaluation considers tiles used in multiple 4-in-a-row combinations as more valuable
     */
    private int evalGame(int[] gameBoard) {

        int fourCellEval = 0;

        // Horizontal check
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7-3; col++) {
                fourCellEval += (gameBoard[row * 7 + col] % 2) * 2 - 1;
                fourCellEval += (gameBoard[row * 7 + col + 1] % 2) * 2 - 1;
                fourCellEval += (gameBoard[row * 7 + col + 2] % 2) * 2 - 1;
                fourCellEval += (gameBoard[row * 7 + col + 3] % 2) * 2 - 1;
            }
        }

        // Vertical check
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6-3; row++) {
                fourCellEval += (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col + (row + 1) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col + (row + 2) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col + (row + 3) * 7] % 2) * 2 - 1;
            }
        }

        // diagonal (down + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 0; row < 6-3; row++){
                fourCellEval += (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 1 + (row + 1) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 2 + (row + 2) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 3 + (row + 3) * 7] % 2) * 2 - 1;
            }
        }

        // diagonal (up + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 3; row < 6; row++){
                fourCellEval += (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 1 + (row - 1) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 2 + (row - 2) * 7] % 2) * 2 - 1;
                fourCellEval += (gameBoard[col - 3 + (row - 3) * 7] % 2) * 2 - 1;
            }
        }
//
//        if (isPlayerOne == false)
//            fourCellEval *= -1;

        return fourCellEval;
    }

    
}
