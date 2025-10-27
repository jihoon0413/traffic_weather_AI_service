package com.capstone.ai_model.reader;

import com.capstone.ai_model.dto.FeaturedCongestionData;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
public class DbItemReader {

    @Bean
    public JdbcPagingItemReader<FeaturedCongestionData> jdbcPagingItemReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<FeaturedCongestionData>()
                .name("dbItemReader")
                .dataSource(dataSource)
                .selectClause("*")
                .fromClause("featured_congestion_data")
                .sortKeys(Map.of("stat_idx", Order.ASCENDING, "morning", Order.DESCENDING, "record_date", Order.ASCENDING))
                .rowMapper(new BeanPropertyRowMapper<>(FeaturedCongestionData.class))
                .pageSize(10)
                .build();
    }

}
