package com.example.ai_for_data_science.players.algorithms.svm;


import org.apache.commons.math3.linear.MatrixUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SVM {

    private double[][] currentScores;
    private ScorePlayers scorePlayers;
    private AlgorithmsWinningScores algScores;

    private SupportVectorMachine svm = null;
    static double[][][] trainingDataSet =
            {
                    {{1.1789285714285713, 1.6170238095238096}, {-1}},
                    {{1.090892857142857, 1.0250238095238096}, {-1}},
                    {{0.8719940476190476, 1.4050714285714285}, {1}},
                    {{0.488735119047619, 0.7267976190476191}, {-1}},
                    {{0.4607961309523809, 0.919797619047619}, {1}},
                    {{0.509326636904762, 0.6699464285714286}, {1}},
                    {{0.5607552083333334, 0.6075208333333333}, {1}},
                    {{0.5850204613095238, 0.6388080357142857}, {1}},
                    {{0.33120070684523806, 0.3899520089285714},{-1}},
                    {{0.420258556547619, 0.5383802083333333}, {-1}}
            };

    public SVM() {
        this.scorePlayers = new ScorePlayers();
        this.algScores = new AlgorithmsWinningScores();
        this.currentScores = algScores.getPlayersScoreForNextGamePrediction("BayesianClassifier", "Human");
        train();
        printPrediction();

    }

    private int predictNewGame() {
        return this.svm.classify(MatrixUtils.createRealMatrix(currentScores));
    }

    private void printPrediction() {
        String b = "The BayesianClassifier should win this new Game";
        String h = "The Human should win this new Game";
        System.out.println(predictNewGame() == 1 ? b : h);
    }

    public void train() {
        double[][][] trainingSet = getTrainingDataSet();
        //double[][][] trainingSet = trainingDataSet;
        double[][] features = new double[trainingSet.length][2];
        double[][] labels = new double[trainingSet.length][1];

        for (int i = 0; i < trainingSet.length; i++) {
            setFeaturesAndLabels(trainingSet, features, labels, i);
        }
        System.out.println(Arrays.deepToString(trainingSet));
        System.out.println(Arrays.deepToString(features));
        System.out.println(Arrays.deepToString(labels));

        this.svm = new SupportVectorMachine(MatrixUtils.createRealMatrix(features), MatrixUtils.createRealMatrix(labels));

        displayInfoTables(features, labels);
    }

    private static void setFeaturesAndLabels(double[][][] trainingData, double[][] features, double[][] labels, int i) {
        features[i][0] = trainingData[i][0][0];
        features[i][1] = trainingData[i][0][1];
        labels[i][0] = trainingData[i][1][0];
    }

    private void displayInfoTables(double[][] features, double[][] labels) {
        System.out.println(" Support Vector | label | alpha");
        IntStream.range(0, 40).forEach(i -> System.out.print("-"));
        System.out.println();
        for (int i = 0; i < features.length; i++) {
            if (this.svm.getΑ().getData()[i][0] > 0.000009 && this.svm.getΑ().getData()[i][0] != this.svm.SOFT_PARAM_C) {
                StringBuffer ySB = new StringBuffer(String.valueOf(labels[i][0]));
                ySB.setLength(5);
                System.out.println(Arrays.toString(features[i]) + " | " + ySB + " | " +
                        new String(String.format("%.10f", svm.getΑ().getData()[i][0])));
            }
        }
        System.out.println("\n             wT              |  b  ");
        IntStream.range(0, 40).forEach(i -> System.out.print("-"));
        System.out.println();
        System.out.println("<" + (new String(String.format("%.9f", svm.getW().getData()[0][0])) + ", "
                + new String(String.format("%.9f", svm.getW().getData()[1][0]))) + ">   | " + svm.getB());
    }

    private double[][][] getTrainingDataSet() {
        return scorePlayers.getTrainingData();
//        double[][][] data = scorePlayers.getTrainingData();
//        System.out.println(Arrays.deepToString(data));
//        double[][][] trainingData = new double[data.length][][];
//        for (int i = 0; i < data.length; i++){
//            for(int j = 0; j < data[i].length; j++){
//                for(int k = 0; k < data[i][j].length; k++){
//                    System.out.println(data[i][j][k]);
//                    System.out.println(Double.isFinite(data[i][j][k]));
//                    trainingData[i][j][k] = data[i][j][k];
//                    System.out.println(trainingData[i][j][k]);
//                }
//            }
//        }
//        return trainingData;
    }

    public static void main(String[] args) {
        new SVM();
    }
}

