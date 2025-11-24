package com.capstone.ai_model.training.model;

import com.capstone.ai_model.service.ChartService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelEvaluator {

    private final ChartService chartService;

    public void evaluateSeason(ComputationGraph model, MultiDataSet test, String tag) throws IOException {
        INDArray predicted = model.output(test.getFeatures())[0];
        INDArray real = test.getLabels()[0];

        INDArray predFlat = predicted.ravel();
        INDArray realFlat = real.ravel();

        chartService.setData(realFlat, predFlat);
    }
}
