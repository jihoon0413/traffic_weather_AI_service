package com.capstone.ai_model.config;

import com.capstone.ai_model.dto.BusWeatherData;
import com.capstone.ai_model.dto.FeatureData;
import com.capstone.ai_model.preprocessing.processor.BusWeatherItemProcessor;
import com.capstone.ai_model.preprocessing.processor.DataPreProcessor;
import com.capstone.ai_model.preprocessing.writer.GenerateCongestionDataWriter;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
public class GenerateCongestionDataJob {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job busWeatherDataJob(@Qualifier("busWeatherDataPreStep") Step busWeatherDataPreStep,
                                 @Qualifier("busWeatherDataStep") Step busWeatherDataStep) {
        return new JobBuilder("busWeatherDataJob", jobRepository)
                .start(busWeatherDataPreStep)
                .next(busWeatherDataStep)
                .build();
    }

    @Bean // 정규화 임베딩에 필요한 데이터 준비하는 Step
    public Step busWeatherDataPreStep(FlatFileItemReader<BusWeatherData> reader,
                                      DataPreProcessor processor,
                                      BusWeatherDataItemWriter writer) {
        return new StepBuilder("busWeatherDataPreStep", jobRepository)
                .<BusWeatherData, BusWeatherData>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean // INDArray로 바꾸기 위해 정규화, 임베딩 처리 Step
    public Step busWeatherDataStep(FlatFileItemReader<BusWeatherData> reader,
                                   BusWeatherItemProcessor processor,
                                   GenerateCongestionDataWriter writer) {
        return new StepBuilder("busWeatherDataStep", jobRepository)
                .<BusWeatherData, FeatureData>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public BusWeatherDataItemWriter busWeatherDataItemWriter() {
        return new BusWeatherDataItemWriter();
    }   //TODO: writer 개발

    public static class BusWeatherDataItemWriter implements ItemWriter<Object> {
        @Override
        public void write(Chunk<?> chunk) throws Exception {
            for (Object data : chunk) {
                if(data instanceof FeatureData) {
//                    log.info("data : {}", data);
                }
            }
        }
    }
}
