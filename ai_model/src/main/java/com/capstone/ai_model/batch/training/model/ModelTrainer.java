package com.capstone.ai_model.batch.training.model;

import com.capstone.ai_model.dto.LSTMInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelTrainer {

    private final ComputationGraph model;
    private final ModelEvaluator modelEvaluator;

    public void trainAndEvaluate(
            List<LSTMInput> trainSummer,
            List<LSTMInput> testSummer,
            List<LSTMInput> trainWinter,
            List<LSTMInput> testWinter
    ) throws IOException {
        MultiDataSet trainData = mergeData(trainSummer, trainWinter);

        for (int epoch = 0; epoch < 80; epoch++) {
            model.fit(trainData);
            System.out.println("Epoch = " + epoch + " 완료");
        }

        modelEvaluator.evaluateSeason(model, mergeData(testSummer, testWinter), "total");

        model.save(new File("trained_lstm_model.zip"),true);
    }

    private MultiDataSet mergeData(List<LSTMInput> trainSummer, List<LSTMInput> trainWinter) {
        List<INDArray> stationList = new ArrayList<>();
        List<INDArray> featureList = new ArrayList<>();
        List<INDArray> targetList = new ArrayList<>();

        for (LSTMInput input : trainWinter) {
            stationList.add(input.getStatIdx());
            featureList.add(input.getFeatures());
            targetList.add(input.getTargets());
        }
        for (LSTMInput input : trainSummer) {

            stationList.add(input.getStatIdx());
            featureList.add(input.getFeatures());
            targetList.add(input.getTargets());
        }

        INDArray stationBatch = Nd4j.concat(0, stationList.toArray(new INDArray[0]));
        INDArray featureBatch = Nd4j.concat(0, featureList.toArray(new INDArray[0])); // [batch, FEATURE_SIZE, seqLength]
        INDArray targetBatch = Nd4j.concat(0, targetList.toArray(new INDArray[0])); // [batch, 1, seqLength]

        return new MultiDataSet(
                new INDArray[]{stationBatch, featureBatch},
                new INDArray[]{targetBatch}
        );
    }
}
