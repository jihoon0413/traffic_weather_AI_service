package com.capstone.ai_model.controller;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictController {

    private ComputationGraph model;

    public void setModel(ComputationGraph model) {
        this.model = model;
    }

    @GetMapping
    public void predictCongestion() {
//        model.output();
    }

}
