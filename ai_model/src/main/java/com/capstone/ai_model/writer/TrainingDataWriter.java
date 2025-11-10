package com.capstone.ai_model.writer;

import com.capstone.ai_model.dto.LSTMInput;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class TrainingDataWriter implements ItemWriter<LSTMInput> {

    private final MultiLayerNetwork model;

    @Override
    public void write(Chunk<? extends LSTMInput> chunk) throws Exception {

        log.info("===============start training=============");
        for (LSTMInput input : chunk) {
//            MultiDataSet ds = new MultiDataSet(
//                    new INDArray[]{input.getStatIdx(), input.getFeatures()},
//                    new INDArray[]{input.getTargets()});
            DataSet ds = new DataSet(input.getFeatures(), input.getTargets());
            model.fit(ds);
        }

        model.save(new File("trained_lstm_model.zip"),true);
    }
}
