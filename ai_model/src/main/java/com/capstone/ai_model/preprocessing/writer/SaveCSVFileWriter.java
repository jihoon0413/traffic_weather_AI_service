package com.capstone.ai_model.preprocessing.writer;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@Configuration
public class SaveCSVFileWriter {

    @Bean
    public FlatFileItemWriter<FeaturedCongestionData> tempDataWriter() {
        return new FlatFileItemWriterBuilder<FeaturedCongestionData>()
                .name("FeatureCongestionDateWriter")
                .resource(new FileSystemResource("C:\\Users\\wlgns\\Desktop\\java\\projects\\traffic_weather_AI_service\\ai_model\\src\\main\\resources\\data\\dataWithCongestion.csv"))
                .encoding("UTF-8")
                .delimited()
                .delimiter(",")
                .sourceType(FeaturedCongestionData.class)
                .names("record_date", "year_value", "monthSin", "monthCos", "summer", "winter",
                        "monday", "tuesday", "wednesday", "thursday", "friday",
                        "busId", "stat_idx", "morning", "evening",
                        "temp", "precip", "snow", "congestion")
                .build();
    }
}
