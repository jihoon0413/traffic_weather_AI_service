package com.capstone.ai_model.training.model;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelEvaluator {
    public void evaluateSeason(ComputationGraph model, MultiDataSet test, String tag) {
        INDArray predicted = model.output(test.getFeatures())[0];
        INDArray real = test.getLabels()[0];

        INDArray predFlat = predicted.reshape(predicted.length());
        INDArray realFlat = real.reshape(real.length());

        double mse = predFlat.squaredDistance(realFlat) / realFlat.length();
        double mae = Transforms.abs(predFlat.sub(realFlat))
                .sumNumber().doubleValue() / realFlat.length();

        System.out.println("===== " + tag + " EVALUATION =====");
        System.out.println("MSE: " + mse);
        System.out.println("MAE: " + mae);

    }
}
