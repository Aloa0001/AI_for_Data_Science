package com.example.ai_for_data_science.players.algorithms;
import com.example.ai_for_data_science.Algorithm;

import java.io.*;
import java.lang.Math;
import java.util.*;


public class LinearRegression implements Algorithm {

    public LinearRegression(float[][] independentFeatures, float[] dependentFeature, float[] weights, float bias,
                            float learningRate, int iterations, int batchSize) {
        this.independentFeatures = independentFeatures;
        this.dependentFeature = dependentFeature;
        this.weights = weights;
        this.bias = bias;
        this.learningRate = learningRate;
        this.iterations = iterations;
        this.batchSize = batchSize;
    }

    private final float[][] independentFeatures;
    private final float[] dependentFeature;
    private float[] weights;
    private float bias;

    private final float learningRate;
    private final int iterations;
    private final int batchSize;


    public float[] getWeights() {
        return weights;
    }
    public float getBias() {
        return bias;
    }


    @Override
    public int returnMove(int[] gameBoard) {
        return 0;
    }

    @Override
    public void printResults() {
        System.out.println("Linear Regression - Results:");
        System.out.println("  Weights: " + Arrays.toString(weights));
        System.out.println("  Bias: " + bias);
        System.out.println("  MSE: " + mseCost());
        System.out.println("  R²: " + Math.round(rSquared() * 100000.0f) / 1000.0f + "%");
    }


    public static void preProcessData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("fourCellEvals.csv", true));

//        for (int i = 0; i < 42; i++) {      // for each cell
//            int totalPlays = 0;
//            int winsP1 = 0;
//
//            Scanner scanner = new Scanner(new File("gameData.csv"));
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String gameResult = line.split(",")[1];
//
//                char cellValue = line.charAt(i);
//                if (cellValue == '1') {
//                    if (gameResult.equals("1")) {
//                        ++winsP1;
//                    }
//                    ++totalPlays;
//                }
//            }
//
//            String winRate = totalPlays == 0 ? "" : String.valueOf((double)winsP1/totalPlays);
//
//            writer.write(String.format("%d,%d,%d,%s\n", i, i % 7, totalPlays, winRate));
//        }
//        writer.close();

        ArrayList<Integer> fourCellEvals = new ArrayList<>();
        ArrayList<Integer> wins = new ArrayList<>();
        ArrayList<Integer> totalPlays = new ArrayList<>();

        Scanner scanner = new Scanner(new File("gameData.csv"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] data = line.split(",");

            String gameBoardString = data[0];
            String gameResult = data[1];

            int[] gameBoard = new int[42];
            for (int j = 0; j < gameBoardString.length(); j++) {
                gameBoard[j] = Character.getNumericValue(gameBoardString.charAt(j));
            }

            int fourCellEval = fourCellEval(gameBoard);
            int index;
            if ((index = fourCellEvals.indexOf(fourCellEval)) != -1) {
                if (gameResult.equals("1")) {
                    wins.set(index, wins.get(index) + 1);
                }
                totalPlays.set(index, wins.get(index) + 1);
            }
            else {
                fourCellEvals.add(fourCellEval);
                wins.add(gameResult.equals("1") ? 1 : 0);
                totalPlays.add(1);
            }
        }
        for (int i = 0; i < fourCellEvals.size(); i++) {
            String winRate = totalPlays.get(i) == 0 ? "" : String.valueOf((double)wins.get(i)/totalPlays.get(i));
            writer.write(String.format("%d,%s\n", fourCellEvals.get(i), winRate));
        }
        writer.close();
    }

    /**
     * Evaluates how good a not won/tied position is.
     * Checks each combination of 4 tiles in a row, and sums the number of player1 discs - player2 discs
     * This means the evaluation considers tiles used in multiple 4-in-a-row combinations as more valuable
     */
    private static int fourCellEval(int[] gameBoard) {
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

        return fourCellEval;
    }
    private static int remapCellValue(int value) {
        return (value == 2) ? -1 : value;  // 0->0, 1->1, 2->-1
    }



    private float[] predict() {
        int n = independentFeatures.length;
        int m = independentFeatures[0].length;
        float[] sums = new float[n];

        for (int i = 0; i < n; i++) {
            sums[i] = bias;
            for (int j = 0; j < m; j++) {
                sums[i] += independentFeatures[i][j] * weights[j];
            }
        }
        return sums;
    }
    private float predict(float[] independentFeature) {
        float sum = bias;
        for (int j = 0; j < independentFeature.length; j++) {
            sum += independentFeature[j] * weights[j];
        }
        return sum;
    }

    private float mseCost() {
        float[] squareErrors = pow(subtract(predict(), dependentFeature), 2);
        return sum(divide(squareErrors, (2 * dependentFeature.length)));
    }

    private float rSquared() {
        float residual_sum_of_squares = 0.0f;
        float total_sum_of_squares = 0.0f;

        float[] predictions = predict();
        float mean = sum(dependentFeature) / dependentFeature.length;

        residual_sum_of_squares = sum(pow(subtract(predictions, dependentFeature), 2));
        total_sum_of_squares =    sum(pow(subtract(predictions, mean),             2));

        return 1 - residual_sum_of_squares / total_sum_of_squares;
    }

    private void updateWeights() {
        int[] randomIndices = new int[batchSize];
        float[] predictions = new float[batchSize];

        ArrayList<Integer> indicesSampleSpace = new ArrayList<Integer>(independentFeatures.length);
        for(int i = 0; i < independentFeatures.length; i++) {
            indicesSampleSpace.add(i);
        }
        Random r = new Random();

        for (int i = 0; i < batchSize; i++) {
            randomIndices[i] = indicesSampleSpace.get(r.nextInt(indicesSampleSpace.size()));
            indicesSampleSpace.remove((Integer)randomIndices[i]);
            predictions[i] = predict(independentFeatures[randomIndices[i]]);
        }

        float[] d_dependentFeatures = new float[weights.length];
        float d_bias = 0.0f;

        for (int i = 0; i < batchSize; i++) {
            d_dependentFeatures = subtract(d_dependentFeatures, multiply(independentFeatures[randomIndices[i]], dependentFeature[randomIndices[i]] - predictions[i]));
            d_bias -= dependentFeature[randomIndices[i]] - predictions[i];
        }

        weights = subtract(weights, multiply(d_dependentFeatures, learningRate));
        bias -= d_bias * learningRate;
    }

    public void train() {
        for (int i = 0; i < iterations; i++) {
            updateWeights();
        }
    }


    private float[] subtract(float[] arr1, float[] arr2) {
        int n = arr1.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = arr1[i] - arr2[i];
        }
        return result;
    }

    private float[] subtract(float[] arr1, float a) {
        int n = arr1.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = arr1[i] - a;
        }
        return result;
    }

    private float[] pow(float[] arr1, float exponent) {
        int n = arr1.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = (float)Math.pow(arr1[i], exponent);
        }
        return result;
    }

    private float[] multiply(float[] arr1, float a) {
        int n = arr1.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = arr1[i] * a;
        }
        return result;
    }

    private float[] divide(float[] arr1, float a) {
        int n = arr1.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = arr1[i] / a;
        }
        return result;
    }

    private float sum(float[] arr1) {
        float sum = 0.0f;
        for (int i = 0; i < arr1.length; i++) {
            sum += arr1[i];
        }
        return sum;
    }
}
