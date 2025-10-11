package com.capstone.ai_model.reader;

import com.capstone.ai_model.dto.BusWeatherData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class ItemReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<BusWeatherData> busWeatherDataItemReader() {
        return new FlatFileItemReaderBuilder<BusWeatherData>()
                .name("BusWeatherDataItemReader")
                .resource(new ClassPathResource("busWeatherData.csv"))
                .encoding("UTF-8")
                .delimited()
                .names("busStatId", "seq", "busStopName", "busId", "busName", "date",
                        "commuteOnPassengers", "commuteOffPassengers", "offPeakOnPassengers", "offPeakOffPassengers",
                        "morning_avg_temp_c", "morning_avg_precip_mm", "morning_avg_snow_cm",
                        "evening_avg_temp_c", "evening_avg_precip_mm", "evening_avg_snow_cm")
                .targetType(BusWeatherData.class)
                .linesToSkip(1)
                .strict(true)
                .customEditors(Map.of(LocalDateTime.class, dateTimeEditor()))
                .build();
    }

    private PropertyEditor dateTimeEditor() {
        return new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                setValue(LocalDateTime.parse(text, formatter));
            }
        };
    }
}
