package com.capstone.ai_model.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class TrainingData {
    double[] morningFeature;
    int morningCongestion;
    double[] eveningFeature;
    int eveningCongestion;
}
