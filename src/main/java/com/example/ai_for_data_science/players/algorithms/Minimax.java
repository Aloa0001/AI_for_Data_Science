package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class Minimax implements Algorithm {
    static final int depth = -1; // -1 means infinite depth


//    gameBoard translation into int[]
//    0 - empty
//    1 - your disc
//    2 - opponent

//        0  1  2  3  4  5  6
//        7  8  9  10 11 12 13
//        14 15 16 17 18 19 20
//        21 22 23 24 25 26 27
//        28 29 30 31 32 33 34
//        35 36 37 38 39 40 41

    @Override
    public int returnMove(int[] gameBoard) {

        int bestEval = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col : getAvailableMoves(gameBoard))
        {
            int moveEval = minimax(gameBoard, depth, true);

            if (moveEval > bestEval) {
                bestMove = col;
                bestEval = moveEval;
            }
        }

        return bestMove;
    }


    private int minimax(int[] gameBoard, int depth, boolean maximizingPlayer){

        if (depth == 0 || gameIsFinished(gameBoard) > -1) return evalGameState(gameBoard);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int col : getAvailableMoves(gameBoard)) {
                int moveEval = minimax(nextGameBoard(gameBoard, col, false /*TODO: Determine if this is the first or second player*/), depth-1, false);
                maxEval = Math.max(maxEval, moveEval);
            }

            return maxEval;
        }
        else
        {
            int minEval = Integer.MAX_VALUE;

            for (int col: getAvailableMoves(gameBoard)) {
                int moveEval = minimax(nextGameBoard(gameBoard ,col, true /*TODO: Determine if this is the first or second player*/), depth-1, true);
                minEval = Math.min(minEval, moveEval);
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
     * Evaluates how good the position is.
     * The best position is the one where player 1 won, and the worst is when player 2 won
     */
    private int evalGameState(int[] gameBoard) {

        int eval = gameIsFinished(gameBoard);
        if (eval == 2) eval = -1; //assigns negative score to the position where player 2 won
        return eval;
    }

    /**
     * Evaluates if any player has won the game
     * @param gameBoard The gameBoard that is evaluated
     * @return -1: the game has _not_ been finished, 0: the game is a tie, 1: player 1 won, 2: player2 won
     */
    private int gameIsFinished(int[] gameBoard){

        // Horizontal check
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7-3; col++) {
                if (gameBoard[row * 7 + col] == 1 &&
                        gameBoard[row * 7 + col + 1] == 1 &&
                        gameBoard[row * 7 + col + 2] == 1 &&
                        gameBoard[row * 7 + col + 3] == 1){
                    return 1; // player 1 has 4 horizontal discs in a row
                }
                if (gameBoard[row * 7 + col] == 2 &&
                        gameBoard[row * 7 + col + 1] == 2 &&
                        gameBoard[row * 7 + col + 2] == 2 &&
                        gameBoard[row * 7 + col + 3] == 2){
                    return 2; // player 2 has 4 horizontal discs in a row
                }
            }
        }

        // Vertical check
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6-3; row++) {
                if (gameBoard[col + row * 7] == 1 &&
                        gameBoard[col + (row + 1) * 7] == 1 &&
                        gameBoard[col + (row + 2) * 7] == 1 &&
                        gameBoard[col + (row + 3) * 7] == 1){
                    return 1; // player 1 has 4 vertical discs in a row
                }
                if (gameBoard[col + row * 7] == 2 &&
                        gameBoard[col + (row + 1) * 7] == 2 &&
                        gameBoard[col + (row + 2) * 7] == 2 &&
                        gameBoard[col + (row + 3) * 7] == 2){
                    return 2; // player 2 has 4 vertical discs in a row
                }
            }
        }

        // diagonal (down + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 0; row < 6-3; row++){
                if (gameBoard[col + row * 7] == 1 &&
                        gameBoard[col - 1 + (row + 1) * 7] == 1 &&
                        gameBoard[col - 2 + (row + 2) * 7] == 1 &&
                        gameBoard[col - 3 + (row + 3) * 7] == 1){
                    return 1; // player 1 has 4 diagonal discs in a row
                }
                if (gameBoard[col + row * 7] == 2 &&
                        gameBoard[col - 1 + (row + 1) * 7] == 2 &&
                        gameBoard[col - 2 + (row + 2) * 7] == 2 &&
                        gameBoard[col - 3 + (row + 3) * 7] == 2){
                    return 2; // player 2 has 4 diagonal discs in a row
                }
            }
        }
        // diagonal (up + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 3; row < 6; row++){
                if (gameBoard[col + row * 7] == 1 &&
                        gameBoard[col - 1 + (row - 1) * 7] == 1 &&
                        gameBoard[col - 2 + (row - 2) * 7] == 1 &&
                        gameBoard[col - 3 + (row - 3) * 7] == 1){
                    return 1; // player 1 has 4 diagonal discs in a row
                }
                if (gameBoard[col + row * 7] == 2 &&
                        gameBoard[col - 1 + (row - 1) * 7] == 2 &&
                        gameBoard[col - 2 + (row - 2) * 7] == 2 &&
                        gameBoard[col - 3 + (row - 3) * 7] == 2){
                    return 2; // player 2 has 4 diagonal discs in a row
                }
            }
        }

        // not a tie check
        for (int i = 0; i < 7; i++) {
            if (gameBoard[i] == 0) { // found a tile where a disc can be placed => the game has _not_ been finished
                return -1;
            }
        }

        return 0; // if no player has won, and no more tiles can be placed => game resulted in a tie
    }


    /**
     * Returns how the next gameBoard will look, after the inputted move has been made.
     * Used by the minimax-algorithm to analyze the game in greater depth
     * @param gameBoard The current gameBoard
     * @param moveCol The move to make
     * @param isFirstPlayer Whether this player is player 1 or 2
     * @return The next gameBoard, after the move has been made
     */
    public int[] nextGameBoard(int[] gameBoard, int moveCol, boolean isFirstPlayer){
        int[] nextGameBoard = Arrays.copyOf(gameBoard, gameBoard.length);

        for (int i = moveCol + 35; i >= moveCol; i -= 7) {
            if (gameBoard[i] == 0) {
                nextGameBoard[i] = isFirstPlayer ? 1 : 2;
                return nextGameBoard;
            }
        }
        return null;
    }
    
}
