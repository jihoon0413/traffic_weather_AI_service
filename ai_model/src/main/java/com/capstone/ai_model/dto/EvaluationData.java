package com.capstone.ai_model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EvaluationData {
    private double mae;
    private double mse;
    private double rmse;
    private double smape;
    private double r2;
}
