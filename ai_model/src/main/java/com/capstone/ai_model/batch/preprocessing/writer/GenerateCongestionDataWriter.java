package com.capstone.ai_model.batch.preprocessing.writer;

import com.capstone.ai_model.dto.FeatureData;
import com.capstone.ai_model.domain.FeaturedCongestionData;
import com.capstone.ai_model.repository.FeaturedDataRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class GenerateCongestionDataWriter implements ItemWriter<FeatureData> {

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

//    private final DateTimeFormatter formatter;
//    private final FlatFileItemWriter<FeaturedCongestionData> delegate;
//
//    @Override
//    public void write(Chunk<? extends FeatureData> chunk) throws Exception {
//
//        Chunk<FeaturedCongestionData> list = new Chunk<>();
//        for(FeatureData data : chunk) {
//            log.info("I am Writer");
//            list.add(FeaturedCongestionData.ofMorning(data, formatter));
//            list.add(FeaturedCongestionData.ofEvening(data, formatter));
//        }
//        ExecutionContext executionContext = new ExecutionContext();
//        delegate.afterPropertiesSet();
//        delegate.open(executionContext);
//        delegate.write(list);
//    }
}
