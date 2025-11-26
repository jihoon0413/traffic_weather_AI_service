package com.capstone.ai_model.utils;

public class DensityUtil {

    public static double[] computeDensity(double[] x, double[] y, double bandwidth) {

        int n = x.length;
        double[] density = new double[n];

        for (int i = 0; i < n; i++) {
            double sum = 0;

            for (int j = 0; j < n; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double distSq = dx * dx + dy * dy;

                sum += Math.exp(-distSq / (2 * bandwidth * bandwidth));
            }
            density[i] = sum;
        }

        return density;
    }
}
