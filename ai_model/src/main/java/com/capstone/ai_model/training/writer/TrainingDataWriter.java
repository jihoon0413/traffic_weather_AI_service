package com.capstone.ai_model.training.writer;

import com.capstone.ai_model.dto.training.LSTMInput;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class TrainingDataWriter implements ItemWriter<LSTMInput> {

//    private final MultiLayerNetwork model;
    private final ComputationGraph model;

    @Override
    public void write(Chunk<? extends LSTMInput> chunk) throws Exception {

        if(chunk.isEmpty()) return;

        List<INDArray> stationList = new ArrayList<>();
        List<INDArray> featureList = new ArrayList<>();
        List<INDArray> targetList = new ArrayList<>();

        for (LSTMInput input : chunk) {
            stationList.add(input.getStatIdx());
            featureList.add(input.getFeatures());
            targetList.add(input.getTargets());
        }

        // 배치 단위로 합치기
        INDArray stationBatch = Nd4j.concat(0, stationList.toArray(new INDArray[0]));
        INDArray featureBatch = Nd4j.concat(0, featureList.toArray(new INDArray[0])); // [batch, FEATURE_SIZE, seqLength]
        INDArray targetBatch = Nd4j.concat(0, targetList.toArray(new INDArray[0])); // [batch, 1, seqLength]

        MultiDataSet ds = new MultiDataSet(
                new INDArray[]{stationBatch, featureBatch},
                new INDArray[]{targetBatch}
        );
        log.info("i'm training");
        // 모델 학습
        model.fit(ds);
        model.save(new File("trained_lstm_model.zip"),true);
    }
}
