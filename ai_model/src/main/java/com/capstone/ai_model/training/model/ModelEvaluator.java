package com.capstone.ai_model.training.model;

import com.capstone.ai_model.utils.PlotUtils;
import java.io.IOException;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelEvaluator {
    public void evaluateSeason(ComputationGraph model, MultiDataSet test, String tag) throws IOException {
        INDArray predicted = model.output(test.getFeatures())[0];
        INDArray real = test.getLabels()[0];

        INDArray predFlat = predicted.ravel();
        INDArray realFlat = real.ravel();

        PlotUtils.plotRealVsPred(realFlat, predFlat, "Real vs Predicted");
        PlotUtils.plotScatterRealPred(realFlat, predFlat, "Scatter Plot");
        PlotUtils.plotResiduals(realFlat, predFlat, "Residual Plot");
        PlotUtils.plotResidualHistogram(realFlat, predFlat, "Residual Distribution");

        // diff
        INDArray diff = predFlat.sub(realFlat).dup();

        // MAE
        INDArray absDiff = Transforms.abs(diff);
        double mae = absDiff.meanNumber().doubleValue();

        // MSE / RMSE
        INDArray sq = diff.mul(diff);
        double mse = sq.meanNumber().doubleValue();
        double rmse = Math.sqrt(mse);

        // sMAPE
        double eps = 1e-6;
        INDArray denominator = Transforms.abs(predFlat).add(Transforms.abs(realFlat)).add(eps);
        INDArray smapeArray = absDiff.mul(2).div(denominator);
        double smape = smapeArray.meanNumber().doubleValue() * 100;

        // R²
        double meanReal = realFlat.meanNumber().doubleValue();
        INDArray centered = realFlat.sub(meanReal);
        double ssTot = centered.mul(centered).sumNumber().doubleValue();
        double ssRes = sq.sumNumber().doubleValue();
        double r2 = 1.0 - ssRes / ssTot;

        System.out.println("===== " + tag + " =====");
        System.out.println("MAE  : " + mae);
        System.out.println("MSE  : " + mse);
        System.out.println("RMSE : " + rmse);
        System.out.println("sMAPE: " + smape);
        System.out.println("R^2  : " + r2);

    }
}
