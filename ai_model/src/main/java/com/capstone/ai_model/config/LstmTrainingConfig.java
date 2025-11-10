package com.capstone.ai_model.config;

import com.capstone.ai_model.config.GenerateCongestionDataConfig.BusWeatherDataItemWriter;
import com.capstone.ai_model.dto.FeatureData;
import com.capstone.ai_model.dto.FeaturedCongestionData;
import com.capstone.ai_model.dto.LSTMInput;
import com.capstone.ai_model.processor.LstmDataProcessor;
import com.capstone.ai_model.writer.TrainingDataWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
public class LstmTrainingConfig {

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
    public Step trainingDataStep(JdbcPagingItemReader<FeaturedCongestionData> reader,
                                LstmDataProcessor processor,
                                TrainingDataWriter writer) {
        return new StepBuilder("trainingDataStep", jobRepository)
                .<FeaturedCongestionData, LSTMInput>chunk(350, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }




}
