package com.capstone.ai_model.training.model;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.layers.EmbeddingSequenceLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LstmModel {

    @Bean
    public static ComputationGraph createLstmModel() {
        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Adam(0.001))
                .graphBuilder()
                .addInputs("stationInput", "weatherInput")
                .addLayer("stationEmbedding", new EmbeddingSequenceLayer.Builder()
                        .nIn(118)
                        .nOut(20)
                        .build(), "stationInput")
                .addVertex("concat", new MergeVertex(), "stationEmbedding", "weatherInput")
                .addLayer("lstm", new LSTM.Builder()
                        .nIn(36)
                        .nOut(64)
                        .activation(Activation.TANH)
                        .build(), "concat")
                .addVertex("lastTimeStep", new LastTimeStepVertex("weatherInput"), "lstm")
                .addLayer("output", new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(64)
                        .nOut(1)
                        .build(), "lastTimeStep")
                .setOutputs("output")
                .build();
        ComputationGraph model = new ComputationGraph(conf);
        model.init();
        return model;
    }
}
