package com.capstone.ai_model.training.data;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataLoader {

    public List<FeaturedCongestionData> loadSeasonData(
            JdbcTemplate jdbcTemplate,
            boolean isSummer
    ) {
        String sql;

        if (isSummer) {
            // 여름 데이터만 가져오기
            sql = """
                    SELECT *
                    FROM featured_congestion_data
                    WHERE summer = 1
                      AND record_date BETWEEN '2025-06-01' AND '2025-08-31'
                    ORDER BY record_date ASC, morning DESC, stat_idx ASC
                """;
        } else {
            // 겨울 데이터만 가져오기 (2024-12 ~ 2025-02)
            sql = """
                    SELECT *
                    FROM featured_congestion_data
                    WHERE winter = 1
                      AND record_date BETWEEN '2024-12-01' AND '2025-02-28'
                    ORDER BY record_date ASC, morning DESC, stat_idx ASC
                """;
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FeaturedCongestionData.class));
    }
}
