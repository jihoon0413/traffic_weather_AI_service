package com.capstone.ai_model.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.*;
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

    public static byte[] scatterPlot(INDArray realValues, INDArray predValues) throws IOException {

        double[] real = realValues.toDoubleVector();
        double[] pred = predValues.toDoubleVector();

        XYSeries series = new XYSeries("Pred vs Real");

        for (int i = 0; i < real.length; i++) {
            series.add(real[i],pred[i]);
        }
        XYDataset dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Predicted vs Real",
                "Real",
                "Pred",
                dataset
        );

        // (선택) 점 크기, 색상 조절
        XYPlot plot = chart.getXYPlot();
        XYDotRenderer renderer = new XYDotRenderer();
        renderer.setDotHeight(3);
        renderer.setDotWidth(3);
        plot.setRenderer(renderer);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 800, 600);
        return out.toByteArray();


//        List<Double> realData = new ArrayList<>();
//        List<Double> predData = new ArrayList<>();
//
//        for (int i = 0; i < real.length(); i++) {
//            realData.add(real.getDouble(i));
//            predData.add(pred.getDouble(i));
//        }
//
//        XYChart chart = new XYChartBuilder()
//                .width(600).height(600)
//                .title("Real vs Predicted Scatter")
//                .xAxisTitle("Real")
//                .yAxisTitle("Predicted")
//                .build();
//
//        chart.addSeries("Points", realData, predData);
//
//        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
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

    public static byte[] matlabScatterChart(INDArray realValues, INDArray predValues) throws IOException {
        double[] real = realValues.toDoubleVector();
        double[] pred = predValues.toDoubleVector();

        // 1. 밀도 계산
        double[] density = DensityUtil.computeDensity(real, pred, 1.5);

        double maxD = Arrays.stream(density).max().orElse(1);
        double minD = Arrays.stream(density).min().orElse(0);

        // 2. Dataset 생성
        XYSeries series = new XYSeries("Data");
        for (int i = 0; i < real.length; i++) {
            series.add(real[i], pred[i]);
        }
        XYDataset dataset = new XYSeriesCollection(series);

        // 3. Scatter Plot 생성
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Predicted vs Real (Density)",
                "Real",
                "Pred",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);

        // 4. 점 색상 밀도 기반 적용
        plot.setRenderer(new XYDotRenderer() {
            @Override
            public Paint getItemPaint(int row, int col) {
                double norm = (density[col] - minD) / (maxD - minD);
                return ColorMapUtil.jet(norm);
            }
        });

        // 5. 회귀선 추가
        double[] coef = Regression.linearRegression(real, pred);
        double a = coef[0], b = coef[1];

        XYSeries line = new XYSeries("Regression");
        double minX = Arrays.stream(real).min().orElse(0);
        double maxX = Arrays.stream(real).max().orElse(1);

        line.add(minX, a * minX + b);
        line.add(maxX, a * maxX + b);

        XYSeriesCollection lineDataset = new XYSeriesCollection(line);
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(0, Color.BLACK);

        plot.setDataset(1, lineDataset);
        plot.setRenderer(1, renderer2);

        // 6. R2, RMSE 표시
        double[] yHat = new double[pred.length];
        for (int i = 0; i < pred.length; i++)
            yHat[i] = a * real[i] + b;

        double r2 = Regression.r2(pred, yHat);
        double rmse = Regression.rmse(pred, yHat);

        TextTitle info = new TextTitle(
                String.format("y = %.2fx + %.2f\nR² = %.3f, RMSE = %.3f", a, b, r2, rmse)
        );
        info.setFont(new Font("Arial", Font.PLAIN, 12));
        info.setPosition(RectangleEdge.TOP);
        chart.addSubtitle(info);

        // 7. 이미지 반환
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 900, 700);
        return out.toByteArray();
    }

}
