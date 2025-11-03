package com.capstone.ai_model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;

@Getter
@AllArgsConstructor
public class LSTMInput {
//    private INDArray statIdx;
    private INDArray features;
    private INDArray targets;
}
