package com.capstone.ai_model.writer;

import com.capstone.ai_model.dto.FeatureData;
import com.capstone.ai_model.dto.FeaturedCongestionData;
import com.capstone.ai_model.repository.FeaturedDataRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

//    private final FlatFileItemWriter<FeaturedCongestionData> delegate;
    private final DateTimeFormatter formatter;
    private final FeaturedDataRepository featuredDataRepository;

    @Override
    public void write(Chunk<? extends FeatureData> chunk) throws Exception {

        List<FeaturedCongestionData> saveList = new ArrayList<>();
        for(FeatureData data : chunk) {
            saveList.add(FeaturedCongestionData.ofMorning(data, formatter));
            saveList.add(FeaturedCongestionData.ofEvening(data, formatter));

        }
        featuredDataRepository.saveAll(saveList);
    }
}
