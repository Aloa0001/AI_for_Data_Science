package com.example.ai_for_data_science.players.algorithms;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;


public class LinearRegression {

    public LinearRegression(float[][] independentFeatures, float[] dependentFeature, float[] weights, float bias,
                            float learningRate, int iterations, int batchSize) {
        this.independentFeatures = independentFeatures;
        this.dependentFeature = dependentFeature;
        this.weights = weights;
        this.bias = bias;
        this.learningRate = learningRate;
        this.iterations = iterations;
        this.batchSize =batchSize;
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

    public void print() {
        System.out.println("Linear Regression - Results:");
        System.out.println("  Weights: " + Arrays.toString(weights));
        System.out.println("  Bias: " + bias);
        System.out.println("  MSE: " + mseCost());
        System.out.println("  R²: " + Math.round(rSquared() * 100000.0f) / 1000.0f + "%");
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

        Random r = new Random();

        for (int i = 0; i < batchSize; i++) {
            randomIndices[i] = r.nextInt(independentFeatures.length);
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
