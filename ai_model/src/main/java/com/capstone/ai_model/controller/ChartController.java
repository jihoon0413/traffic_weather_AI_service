package com.capstone.ai_model.controller;

import com.capstone.ai_model.dto.EvaluationData;
import com.capstone.ai_model.service.ChartService;
import com.capstone.ai_model.utils.PlotUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    private ResponseEntity<byte[]> pngResponse(byte[] bytes) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename=\"chart.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }

    @GetMapping("/line")
    public ResponseEntity<byte[]> lineChart() throws IOException {
        return pngResponse(
                PlotUtils.lineChart(chartService.getRealValues(), chartService.getPredValues())
        );
    }

    @GetMapping("/scatter")
    public ResponseEntity<byte[]> scatterChart() throws IOException {
        return pngResponse(
                PlotUtils.scatterPlot(chartService.getRealValues(), chartService.getPredValues())
        );
    }

    @GetMapping("/residuals")
    public ResponseEntity<byte[]> residualsChart() throws IOException {
        return pngResponse(
                PlotUtils.residualPlot(chartService.getRealValues(), chartService.getPredValues())
        );
    }

    @GetMapping("/histogram")
    public ResponseEntity<byte[]> histogramChart() throws IOException {
        return pngResponse(
                PlotUtils.plotResidualHistogram(chartService.getRealValues(), chartService.getPredValues())
        );
    }

    @GetMapping("/evaluation")
    public ResponseEntity<EvaluationData> evaluation() {
        return ResponseEntity.ok().body(chartService.getEvaluation());
    }

}
