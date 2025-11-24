package com.capstone.ai_model.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.nd4j.linalg.api.ndarray.INDArray;

public class PlotUtils {

    public static byte[] lineChart(INDArray real, INDArray pred) throws IOException {

        List<Double> xData = new ArrayList<>();
        List<Double> realData = new ArrayList<>();
        List<Double> predData = new ArrayList<>();

        for (int i = 0; i < real.length(); i++) {
            xData.add((double) i);
            realData.add(real.getDouble(i));
            predData.add(pred.getDouble(i));
        }

        XYChart chart = new XYChartBuilder()
                .width(900)
                .height(400)
                .title("Real vs Predicted")
                .xAxisTitle("Time Step")
                .yAxisTitle("Congestion")
                .build();

        chart.addSeries("Real", xData, realData);
        chart.addSeries("Predicted", xData, predData);

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }

    public static byte[] scatterPlot(INDArray real, INDArray pred) throws IOException {
        List<Double> realData = new ArrayList<>();
        List<Double> predData = new ArrayList<>();

        for (int i = 0; i < real.length(); i++) {
            realData.add(real.getDouble(i));
            predData.add(pred.getDouble(i));
        }

        XYChart chart = new XYChartBuilder()
                .width(600).height(600)
                .title("Real vs Predicted Scatter")
                .xAxisTitle("Real")
                .yAxisTitle("Predicted")
                .build();

        chart.addSeries("Points", realData, predData);

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }

    public static byte[] residualPlot(INDArray real, INDArray pred) throws IOException {
        List<Double> residual = new ArrayList<>();
        List<Double> xData = new ArrayList<>();

        for (int i = 0; i < real.length(); i++) {
            xData.add((double) i);
            residual.add(real.getDouble(i) - pred.getDouble(i));
        }

        XYChart chart = new XYChartBuilder()
                .width(900)
                .height(400)
                .title("Residual Plot")
                .xAxisTitle("Time Step")
                .yAxisTitle("Residual (Real - Pred)")
                .build();

        chart.addSeries("Residuals", xData, residual);

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }

    public static byte[] plotResidualHistogram(INDArray real, INDArray pred) throws IOException {
        List<Double> residual = new ArrayList<>();

        for (int i = 0; i < real.length(); i++) {
            residual.add(real.getDouble(i) - pred.getDouble(i));
        }

        Histogram histogram = new Histogram(residual, 20);

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(400)
                .title("Residual Histogram")
                .xAxisTitle("Residual")
                .yAxisTitle("Count")
                .build();

        chart.addSeries("Residual Dist", histogram.getxAxisData(), histogram.getyAxisData());

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }

}
