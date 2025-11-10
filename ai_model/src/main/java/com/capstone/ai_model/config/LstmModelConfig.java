package com.capstone.ai_model.config;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.EmbeddingSequenceLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LstmModelConfig {

    @Bean
    public static MultiLayerNetwork createLstmModel() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Adam(0.001))
                .list()
                .layer(new LSTM.Builder().nIn(17).nOut(64)
                        .activation(Activation.TANH).build())
                .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(64).nOut(1).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        return model;
//        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
//                .updater(new Adam(0.001))
//                .graphBuilder()
//                .addInputs("stationInput", "weatherInput")
//                .addLayer("stationEmbedding", new EmbeddingLayer.Builder()
//                        .nIn(118)
//                        .nOut(20)
//                        .build(), "stationInput")
//                .addVertex("concat", new MergeVertex(), "stationEmbedding", "weatherInput")
//                .addLayer("dense1", new DenseLayer.Builder()
//                        .nIn(36)
//                        .nOut(64)
//                        .activation(Activation.RELU)
//                        .build(), "concat")
//                .addLayer("output", new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
//                        .activation(Activation.IDENTITY)
//                        .nIn(64)
//                        .nOut(1)
//                        .build(), "dense1")
//                .setOutputs("output")
//                .build();
//
//        ComputationGraph model = new ComputationGraph(conf);
//        model.init();
//        return model;
    }

}
