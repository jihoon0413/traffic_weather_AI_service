package com.capstone.ai_model.training.config;

import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormatterConfig {

    @Bean
    public DateTimeFormatter stringDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }

}
