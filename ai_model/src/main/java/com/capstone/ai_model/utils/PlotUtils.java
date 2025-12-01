package com.capstone.ai_model.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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

        XYSeries series = new XYSeries("Pred vs Real", false);
        for (int i = 0; i < real.length; i++) {
            series.add(real[i], pred[i]);
        }
        XYDataset dataset = new XYSeriesCollection(series);

        // 2. scatter chart 생성
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Scatter Plot of Actual vs Predicted Values", // 논문용 제목
                "Actual (Real)",
                "Predicted (Pred)",
                dataset
        );

        XYPlot plot = chart.getXYPlot();

        // 3. 점 스타일 개선 (크기 + 투명도)
        XYDotRenderer renderer = new XYDotRenderer();
        renderer.setDotHeight(3);
        renderer.setDotWidth(3);
        renderer.setSeriesPaint(0, new Color(255, 0, 0, 80));  // 빨강 + alpha(80)
        plot.setRenderer(renderer);

        // 4. y = x 대각선 추가 (reference line)
        double min = Math.min(Arrays.stream(real).min().orElse(0),
                Arrays.stream(pred).min().orElse(0));
        double max = Math.max(Arrays.stream(real).max().orElse(1),
                Arrays.stream(pred).max().orElse(1));

        XYLineAnnotation diagonal = new XYLineAnnotation(
                min, min,     // 시작점 (x=min, y=min)
                max, max,     // 끝점 (x=max, y=max)
                new BasicStroke(1f),
                new Color(0, 0, 0, 120)  // 얇은 회색(투명)
        );
        plot.addAnnotation(diagonal);

        // 5. Grid/Plot 스타일 미세 조정 (선택)
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 800, 600);
        return out.toByteArray();

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
            residual.add(Math.round((real.getDouble(i) - pred.getDouble(i))*100)/100.0);
        }

        Histogram histogram = new Histogram(residual, 20);

        CategoryChart chart = new CategoryChartBuilder()
                .width(1600)
                .height(800)
                .title("Distribution of Prediction Residuals")
                .xAxisTitle("Residual")
                .yAxisTitle("Count")
                .build();

        chart.addSeries("Residual Dist", histogram.getxAxisData(), histogram.getyAxisData());

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }

}
