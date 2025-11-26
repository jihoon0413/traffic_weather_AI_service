package com.capstone.ai_model.batch.training.model;

import com.capstone.ai_model.service.ChartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        double[] predDouble = predicted.ravel().toDoubleVector();
        double[] realDouble = real.ravel().toDoubleVector();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        map.put("pred", predDouble);
        map.put("real", realDouble);

        mapper.writeValue(new File("prediction_data.json"), map);
    }
}
