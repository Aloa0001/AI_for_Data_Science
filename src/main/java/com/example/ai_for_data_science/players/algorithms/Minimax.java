package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;
import com.example.ai_for_data_science.Connect4;

public class Minimax implements Algorithm {
    final int depth = 7; // -1 means infinite depth
    int nodesExamined = 0;
    int branchesPruned = 0;

    boolean isPlayerOne;

    long startTime;

//        0  1  2  3  4  5  6
//        7  8  9  10 11 12 13
//        14 15 16 17 18 19 20
//        21 22 23 24 25 26 27
//        28 29 30 31 32 33 34
//        35 36 37 38 39 40 41

    public Minimax(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        nodesExamined = 0;
        branchesPruned = 0;
        startTime = System.currentTimeMillis();
    }


    @Override
    public int returnMove(int[] gameBoard) {

        int bestEval = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col : Connect4.getAvailableMoves(gameBoard))
        {
            int moveEval = minimax(Connect4.nextGameBoard(gameBoard, col, isPlayerOne), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (moveEval > bestEval) {
                bestMove = col;
                bestEval = moveEval;
            }
        }

        return bestMove;
    }

    @Override
    public void printResults() {
        System.out.println("\nMinimax results:");
        System.out.println("  Depth: " + depth);
        System.out.println("  Time elapsed: " + ((System.currentTimeMillis() - startTime) / 1000) + " (s)");
        System.out.println("  Nodes Examined: " + nodesExamined);
        System.out.println("  Branches Pruned: " + branchesPruned);
    }

    private int minimax(int[] gameBoard, int depth, int alpha, int beta, boolean maximizingPlayer){

        ++nodesExamined;

        if (Connect4.gameIsFinished(gameBoard) > -1) // if the game finished => evaluate the position to find the next best move
            return evalGameFinished(gameBoard, depth);
        if (depth == 0)     // if the maximum depth was hit => evaluate the position to find the next best move
            return evalGame(gameBoard);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int col : Connect4.getAvailableMoves(gameBoard)) {
                int moveEval = minimax(Connect4.nextGameBoard(gameBoard, col, isPlayerOne), depth-1, alpha, beta, false);
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

            for (int col: Connect4.getAvailableMoves(gameBoard)) {
                int moveEval = minimax(Connect4.nextGameBoard(gameBoard, col, !isPlayerOne), depth-1, alpha, beta, true);
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
     * Evaluates how good a won/tied position is based on who's turn it is and who won
     * The depth ensures that winning faster and losing slower is considered better
     */
    private int evalGameFinished(int[] gameBoard, int depth) {
        int eval = Connect4.gameIsFinished(gameBoard);

        if ((eval == 2 && isPlayerOne) || (eval == 1 && !isPlayerOne)) {
            return -100000 - depth;
        }
        else {
            return 100000 + depth;
        }
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
                fourCellEval += remapCellValue(gameBoard[row * 7 + col]);
                fourCellEval += remapCellValue(gameBoard[row * 7 + col + 1]);
                fourCellEval += remapCellValue(gameBoard[row * 7 + col + 2]);
                fourCellEval += remapCellValue(gameBoard[row * 7 + col + 3]);
            }
        }

        // Vertical check
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6-3; row++) {
                fourCellEval += remapCellValue(gameBoard[col + row * 7]);
                fourCellEval += remapCellValue(gameBoard[col + (row + 1) * 7]);
                fourCellEval += remapCellValue(gameBoard[col + (row + 2) * 7]);
                fourCellEval += remapCellValue(gameBoard[col + (row + 3) * 7]);
            }
        }

        // diagonal (down + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 0; row < 6-3; row++){
                fourCellEval += remapCellValue(gameBoard[col + row * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 1 + (row + 1) * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 2 + (row + 2) * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 3 + (row + 3) * 7]);
            }
        }

        // diagonal (up + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 3; row < 6; row++){
                fourCellEval += remapCellValue(gameBoard[col + row * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 1 + (row - 1) * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 2 + (row - 2) * 7]);
                fourCellEval += remapCellValue(gameBoard[col - 3 + (row - 3) * 7]);
            }
        }

        // System.out.println("gameBoard:  " + Connect4.gameBoardToString(gameBoard) + "    eval:  " + fourCellEval);

        fourCellEval = isPlayerOne ? fourCellEval : -fourCellEval;
        return fourCellEval;
    }


    private int remapCellValue(int value) {
        return (value == 2) ? -1 : value;  // 0->0, 1->1, 2->-1
    }
}
