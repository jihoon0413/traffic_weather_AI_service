package com.capstone.ai_model.config;

import com.capstone.ai_model.dto.BusWeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Job busWeatherDataJob(Step busWeatherDataStep) {
        return new JobBuilder("busWeatherDataJob", jobRepository)
                .start(busWeatherDataStep)
                .build();
    }

    @Bean
    public Step busWeatherDataStep(FlatFileItemReader<BusWeatherData> reader,
                                   BusWeatherDataItemWriter writer) {
        log.info("==========start reader========");
        return new StepBuilder("busWeatherDataStep", jobRepository)
                .<BusWeatherData,BusWeatherData>chunk(10, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                // TODO : processor 추가
                .build();
    }

    @Bean
    public BusWeatherDataItemWriter busWeatherDataItemWriter() {
        return new BusWeatherDataItemWriter();
    }   //TODO: writer 개발

    public static class BusWeatherDataItemWriter implements ItemWriter<BusWeatherData> {
        @Override
        public void write(Chunk<? extends BusWeatherData> chunk) throws Exception {
            for (BusWeatherData data : chunk) {
                log.info("Processing busWeatherData: {}", data);
            }
        }
    }
}
