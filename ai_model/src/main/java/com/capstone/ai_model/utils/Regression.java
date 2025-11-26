package com.capstone.ai_model.utils;

import java.util.Arrays;

public class Regression {

    public static double[] linearRegression(double[] x, double[] y) {
        int n = x.length;

        double sumX = 0, sumY = 0, sumXX = 0, sumXY = 0;
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXX += x[i] * x[i];
            sumXY += x[i] * y[i];
        }

        double a = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double b = (sumY - a * sumX) / n;

        return new double[]{a, b};
    }

    public static double r2(double[] y, double[] yHat) {
        double ssTot = 0, ssRes = 0;

        double mean = Arrays.stream(y).average().orElse(0);

        for (int i = 0; i < y.length; i++) {
            ssRes += Math.pow(y[i] - yHat[i], 2);
            ssTot += Math.pow(y[i] - mean, 2);
        }

        return 1 - (ssRes / ssTot);
    }

    public static double rmse(double[] y, double[] yHat) {
        double sum = 0;
        for (int i = 0; i < y.length; i++) {
            sum += Math.pow(y[i] - yHat[i], 2);
        }
        return Math.sqrt(sum / y.length);
    }
}