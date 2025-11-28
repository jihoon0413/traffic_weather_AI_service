package com.capstone.ai_model.controller;

import com.capstone.ai_model.domain.eNum.SearchTime;
import com.capstone.ai_model.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictController {

    private final PredictService predictService;

    @GetMapping
    public double predictCongestion(@RequestParam("date") String date,
                                 @RequestParam("stopId") int stopId,
                                 @RequestParam("temp") double temp,
                                 @RequestParam("prec") double prec,
                                 @RequestParam("snow") double snow,
                                 @RequestParam("time") SearchTime time) {

        return predictService.predictCongestion(date, stopId, temp, prec, snow, time);

    }

}
