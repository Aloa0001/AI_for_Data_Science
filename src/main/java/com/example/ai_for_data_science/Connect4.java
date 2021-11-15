package com.example.ai_for_data_science;


import com.example.ai_for_data_science.players.algorithms.Minimax;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Connect4 {

    private int[] gameBoard = new int[42];      // Default value is 0
    private ArrayList<Integer> moveOrder = new ArrayList<>();


    public Connect4() {
    }

    public void reset() {
        gameBoard = new int[42];
    }

    public void play(Algorithm player1, Algorithm player2) {
        int gameIsFinished = -1;

        boolean isPlayerOne = true;
        int moveCol;

        do {
            /* Alternate player and get the move decided by the player */
            if (isPlayerOne) {
                moveCol = player1.returnMove(gameBoard);
            }
            else {
                  moveCol = player2.returnMove(gameBoard);
            }

            if (validateMove(gameBoard, moveCol)) {
                performMove(moveCol, isPlayerOne);
                //moveOrder.add(moveCol);
            }
            else {
                try {
                    throw new Exception("Invalid move was played!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            isPlayerOne = !isPlayerOne;

        } while ((gameIsFinished = gameIsFinished(gameBoard)) == -1);


        // The game has finished!
//        System.out.println("\nGame is over! Result: " + gameIsFinished);
//        printGameBoard(gameBoard);
//        System.out.println("Move order: " + moveOrder.toString());
    }


    public static void printGameBoard(int[] gameBoard) {
        System.out.println("____ gameBoard ____");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(gameBoard[j + i * 7] + "  ");
            }
            System.out.println("");
        }
        System.out.println("___________________");
    }

    public static String gameBoardToString(int[] gameBoard) {
        String representaion = "";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                representaion += (gameBoard[j + i * 7]);
            }
        }
        return representaion;
    }


    public static boolean validateMove(int[] gameBoard, int moveCol) {
        return  moveCol >= 0 && moveCol <= 6 && gameBoard[moveCol] == 0;
    }

    private void performMove(int moveCol, boolean isPlayerOne) {
        gameBoard = nextGameBoard(gameBoard, moveCol, isPlayerOne);
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
     * @return A list of valid columns where the disc can be placed
     */
    public static ArrayList<Integer> getAvailableMoves(int[] gameBoard) {
        ArrayList<Integer> moves = new ArrayList<>();
        for (int col = 0; col < 7; col++) {
            if (gameBoard[col] == 0) moves.add(col);
        }
        return moves;
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



//    public  static void collectData(int[] gameBoard, int evalGameFinished) throws IOException {
//        String gameBoardRepresentation = gameBoardToString(gameBoard);
//
//        Scanner scanner = new Scanner(new File("gameData.csv"));
//        int i = 0;
//        boolean found = false;
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            if (line.contains(gameBoardRepresentation + ",")) {
//                found = true;
//                break;
//            }
//            ++i;
//        }
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter("gameData.csv", true));
//
//        int playerOneWins = 0, playerOneLosses = 0, totalGames = 1;
//
//        if (found) {
//            try (Stream<String> lines = Files.lines(Paths.get("gameData.csv"))) {
//                String line_i = lines.skip(i).findFirst().get();
//
//                String[] rows = line_i.split(",");
//                playerOneWins = Integer.parseInt(rows[1]);
//                playerOneLosses = Integer.parseInt(rows[2]);
//                totalGames = Integer.parseInt(rows[3]);
//
//                if (evalGameFinished == 1) playerOneWins++;
//                else playerOneLosses++;
//                totalGames++;
//            }
//        }
//        else {
//
//            if (evalGameFinished == 1) playerOneWins = 1;
//            else playerOneLosses = 1;
//        }
//
//        writer.write(String.format("%s,%d,%d,%d\n", gameBoardToString(gameBoard), playerOneWins, playerOneLosses, totalGames));
//        writer.close();
//    }

    public  static void collectData(int[] gameBoard, int evalGameFinished) throws IOException {
        String gameBoardRepresentation = gameBoardToString(gameBoard);

        Scanner scanner = new Scanner(new File("gameData.csv"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(gameBoardRepresentation + ",")) {
                return;
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("gameData.csv", true));

        String gameResult = "-";    // "t" - tie,  "w" - p1 win,  "l" - p1 lose
        switch (evalGameFinished){
            case 0:
                gameResult = "t";
                break;
            case 1:
                gameResult = "w";
                break;
            case 2:
                gameResult = "l";
                break;
        }

        writer.write(String.format("%s,%s\n", gameBoardToString(gameBoard), gameResult));
        writer.close();
    }

}

