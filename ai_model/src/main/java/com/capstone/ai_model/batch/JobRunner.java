package com.capstone.ai_model.batch;

import com.capstone.ai_model.controller.PredictController;
import com.capstone.ai_model.service.ChartService;
import java.io.File;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final PredictController predictController;
    private final ChartService chartService;

    @Qualifier("busWeatherDataJob")
    private final Job busWeatherDataJob;
    @Qualifier("trainingDataJob")
    private final Job trainingDataJob;

    @Autowired
    public JobRunner(
            JobLauncher jobLauncher, PredictController predictController, ChartService chartService,
            @Qualifier("busWeatherDataJob") Job busWeatherDataJob,
            @Qualifier("trainingDataJob") Job trainingDataJob
    ) {
        this.jobLauncher = jobLauncher;
        this.predictController = predictController;
        this.chartService = chartService;
        this.busWeatherDataJob = busWeatherDataJob;
        this.trainingDataJob = trainingDataJob;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

//        JobExecution execution1 =jobLauncher.run(busWeatherDataJob, params);
//        while (execution1.isRunning()) {
//            Thread.sleep(1000); // 1초 대기
//        }
//        JobExecution exec2 = jobLauncher.run(trainingDataJob, params);
//        waitForCompletion(exec2);

        ComputationGraph model = ComputationGraph.load(new File("trained_lstm_model.zip"), true);
        predictController.setModel(model);
        chartService.setData();

    }

    private void waitForCompletion(JobExecution exec) throws InterruptedException {
        while (exec.isRunning()) {
            Thread.sleep(1000);
        }
    }
}
