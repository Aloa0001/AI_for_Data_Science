package com.example.ai_for_data_science;


import java.util.Arrays;

public class Connect4 {

    private int[] gameBoard = new int[42];


    public Connect4() {

        for (int i = 0; i < gameBoard.length; i++) {
            gameBoard[i] = 0;
        }
    }

    public void play(Algorithm player1, Algorithm player2) {
        int gameIsFinished = -1;

        boolean isPlayerOne = true;
        int moveCol;

        do {
            /* Alternate player and get the move */
            if (isPlayerOne) {
                moveCol = player1.returnMove(gameBoard, true);
            }
            else {

                int[] test_gameBoard = gameBoard;

                for (int x : test_gameBoard) {
                    if(x == 1) {
                        x = 2;
                    }
                    else if (x == 2){
                        x = 1;
                    }
                }

                moveCol = player2.returnMove(test_gameBoard, false);
            }

            if (validateMove(moveCol)) {
                performMove(moveCol, isPlayerOne);
            }
            else {
                // This should never happen! :)    //throw new Exception("Invalid move was played!");
            }

            isPlayerOne = !isPlayerOne;

        } while ((gameIsFinished = gameIsFinished(gameBoard)) == -1);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(gameBoard[j + i * 7]);
            }
            System.out.print("\n");
        }

        System.out.println();
        System.out.println("\n\n Game is over! Result: " + gameIsFinished);
        // Game is finished!

    }


    private Boolean validateMove(int moveCol) {
        return gameBoard[moveCol] == 0;
    }

    private void performMove(int moveCol, boolean isPlayerOne) {
        gameBoard = nextGameBoard(gameBoard, moveCol, isPlayerOne);
    }


    private void print() {

    }


    /**
     * Returns how the next gameBoard will look, after the inputted move has been made.
     * Used by the minimax-algorithm to analyze the game in greater depth
     * @param gameBoard The current gameBoard
     * @param moveCol The move to make
     * @param isPlayerOne Whether the move should be a 1 or 2
     * @return The next gameBoard, after the move has been made
     */
    public static int[] nextGameBoard(int[] gameBoard, int moveCol, boolean isPlayerOne){
        int[] nextGameBoard = Arrays.copyOf(gameBoard, gameBoard.length);

        for (int i = moveCol + 35; i >= moveCol; i -= 7) {
            if (gameBoard[i] == 0) {
                nextGameBoard[i] = isPlayerOne ? 1 : 2;
                return nextGameBoard;
            }
        }
        return null;
    }

    /**
     * Evaluates if any player has won the game
     * @param gameBoard The gameBoard that is evaluated
     * @return -1: the game has _not_ been finished, 0: the game is a tie, 1: player1 won, 2: player2 won
     */
    public static int gameIsFinished(int[] gameBoard){

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


}

