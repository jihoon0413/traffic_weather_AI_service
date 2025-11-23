package com.capstone.ai_model.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
public class LstmTrainingJob {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job trainingDataJob(@Qualifier("trainingDataStep") Step trainingDataStep) {
        return new JobBuilder("trainingDataJob", jobRepository)
                .start(trainingDataStep)
                .build();
    }

    @Bean
    public Step trainingDataStep(Tasklet lstmTrainingTasklet) {
        return new StepBuilder("trainingDataStep", jobRepository)
                .tasklet(lstmTrainingTasklet, platformTransactionManager)
                .build();
    }

}
