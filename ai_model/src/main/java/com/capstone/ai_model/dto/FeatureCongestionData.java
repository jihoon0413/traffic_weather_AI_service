package com.capstone.ai_model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class FeatureCongestionData {
    LocalDate date;
    double[] morningFeature;
    double morningCongestion;
    double[] eveningFeature;
    double eveningCongestion;
}
