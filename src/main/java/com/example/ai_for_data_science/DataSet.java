package com.example.ai_for_data_science;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class DataSet {

    public int[][] trainData_gameBoards;
    public int[] trainData_results;

    public int[][] testData_gameBoards;
    public int[] testData_results;


    public void preProcessing() throws FileNotFoundException {
        ArrayList<int[]> gameBoards = new ArrayList<>();
        ArrayList<Integer> results = new ArrayList<>();

        Scanner scanner = new Scanner(new File("gameData.csv"));
        while (scanner.hasNextLine()) {

            int[] gameBoard = new int[42];
            int result;

            String[] data = scanner.nextLine().split(",");

            for (int i = 0; i < gameBoard.length; i++) {
                gameBoard[i] = Character.getNumericValue((data[0].charAt(i)));
            }
            result = Integer.parseInt(data[1]);

            gameBoards.add(gameBoard);
            results.add(result);
        }

        /* Split the data into train and test sets */
        float trainDataRatio = 0.8f;
        int trainDataLength = (int)(gameBoards.size() * trainDataRatio);
        int testDataLength = gameBoards.size() - trainDataLength;

        trainData_gameBoards = new int[trainDataLength][];
        trainData_results = new int[trainDataLength];

        testData_gameBoards = new int[testDataLength][];
        testData_results = new int[testDataLength];

        ArrayList<Integer> indexList = new ArrayList<>(gameBoards.size());

        for (int i = 0; i < gameBoards.size(); i++) {
            indexList.add(i);
        }
        Collections.shuffle(indexList);

        for (int i = 0; i < trainDataLength; i++) {
            trainData_gameBoards[i] = gameBoards.get(indexList.get(i));
            trainData_results[i] = results.get(indexList.get(i));
        }
        for (int i = trainDataLength; i < testDataLength + trainDataLength; i++) {
            testData_gameBoards[i-trainDataLength] = gameBoards.get(indexList.get(i));
            testData_results[i-trainDataLength] = results.get(indexList.get(i));
        }
    }


    public static void collectData(int[] gameBoard, int evalGameFinished) throws IOException {
        String gameBoardRepresentation = Connect4.gameBoardToString(gameBoard);

        Scanner scanner = new Scanner(new File("gameData.csv"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(gameBoardRepresentation + ",")) {
                return;
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("gameData.csv", true));

        if (evalGameFinished == 2) { evalGameFinished = -1; }

        writer.write(String.format("%s,%s\n", Connect4.gameBoardToString(gameBoard), evalGameFinished));
        writer.close();
    }

}