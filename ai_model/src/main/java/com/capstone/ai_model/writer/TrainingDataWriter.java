package com.capstone.ai_model.writer;

import com.capstone.ai_model.dto.FeatureData;
import com.capstone.ai_model.dto.FeaturedCongestionData;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class TrainingDataWriter implements ItemWriter<FeatureData> {

    private final FlatFileItemWriter<FeaturedCongestionData> delegate;
    private final DateTimeFormatter formatter;

    @Override
    public void write(Chunk<? extends FeatureData> chunk) throws Exception {

        Chunk<FeaturedCongestionData> list = new Chunk<>();
        for(FeatureData data : chunk) {
            log.info("I am Writer");
            list.add(FeaturedCongestionData.ofMorning(data, formatter));
            list.add(FeaturedCongestionData.ofEvening(data, formatter));
        }
        ExecutionContext executionContext = new ExecutionContext();
        delegate.afterPropertiesSet();
        delegate.open(executionContext);
        delegate.write(list);
    }
}
