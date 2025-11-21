package com.capstone.ai_model.training.reader;

import com.capstone.ai_model.dto.training.FeaturedCongestionData;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
public class DbItemReader {

    @Bean
    public JdbcCursorItemReader<FeaturedCongestionData> jdbcPagingItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<FeaturedCongestionData>()
                .name("dbItemReader")
                .dataSource(dataSource)
                .sql("""
                    SELECT *
                    FROM featured_congestion_data
                    ORDER BY stat_idx ASC, record_date ASC, morning DESC
                """)
                .rowMapper(new BeanPropertyRowMapper<>(FeaturedCongestionData.class))
                .build();
    }
}
