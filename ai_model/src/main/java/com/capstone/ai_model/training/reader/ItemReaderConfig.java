package com.capstone.ai_model.training.reader;

import com.capstone.ai_model.dto.training.BusWeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ItemReaderConfig {

    private final DateTimeFormatter formatter;

    @Bean
    @StepScope
    public FlatFileItemReader<BusWeatherData> busWeatherDataItemReader() {
        return new FlatFileItemReaderBuilder<BusWeatherData>()
                .name("BusWeatherDataItemReader")
                .resource(new ClassPathResource("data/busWeatherData.csv"))
                .encoding("UTF-8")
                .delimited()
                .names("busStatId", "seq", "busStopName", "busId", "busName", "date",
                        "commuteOnPassengers", "offPeakOnPassengers", "commuteOffPassengers", "offPeakOffPassengers",
                        "morning_avg_temp_c", "morning_avg_precip_mm", "morning_avg_snow_cm",
                        "evening_avg_temp_c", "evening_avg_precip_mm", "evening_avg_snow_cm")
                .targetType(BusWeatherData.class)
                .linesToSkip(1)
                .strict(true)
                .customEditors(Map.of(LocalDate.class, dateTimeEditor()))
                .build();
    }

    private PropertyEditor dateTimeEditor() {
        return new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, formatter));
            }
        };
    }
}
