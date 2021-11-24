package com.example.ai_for_data_science.players.algorithms.svm;


import org.apache.commons.math3.linear.MatrixUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SVM {

    private double[][] currentScores;
    private ScorePlayers scorePlayers;
    private AlgorithmsWinningScores algScores;

    private SupportVectorMachine svm = null;

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
        //double[][][] trainingSet = getTrainingDataSet();
        TrainingData trainingData = new TrainingData();
        double[][][] trainingSet = trainingData.returnTrainingDataSet();
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

//    public static void main(String[] args) {
//        new SVM();
//    }
}

