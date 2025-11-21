package com.capstone.ai_model.training.launcher;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;

    @Qualifier("busWeatherDataJob")
    private final Job busWeatherDataJob;
    @Qualifier("trainingDataJob")
    private final Job trainingDataJob;

    @Autowired
    public JobRunner(
            JobLauncher jobLauncher,
            @Qualifier("busWeatherDataJob") Job busWeatherDataJob,
            @Qualifier("trainingDataJob") Job trainingDataJob
    ) {
        this.jobLauncher = jobLauncher;
        this.busWeatherDataJob = busWeatherDataJob;
        this.trainingDataJob = trainingDataJob;
    }
    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

//        JobExecution execution1 =jobLauncher.run(busWeatherDataJob, params);
//        while (execution1.isRunning()) {
//            Thread.sleep(1000); // 1초 대기
//        }
        jobLauncher.run(trainingDataJob, params);
    }
}
