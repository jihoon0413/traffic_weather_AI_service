package com.capstone.ai_model.dto.training;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureData {
    LocalDate date;
    double[] morningFeature;
    double morningCongestion;
    double[] eveningFeature;
    double eveningCongestion;
}
