package com.capstone.ai_model.domain;

import com.capstone.ai_model.domain.eNum.SearchTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ModelMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int maxCongestion;
    private double maxTemp;
    private double minTemp;
    private double maxPrecip;
    private double maxSnow;
}
