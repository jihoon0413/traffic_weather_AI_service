package com.capstone.ai_model.reader;

import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ai_model.dto.FeaturedCongestionData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class DbItemReaderTest {

    @Autowired
    private JdbcPagingItemReader<FeaturedCongestionData> reader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE featured_congestion_data");
        jdbcTemplate.update("INSERT INTO featured_congestion_data ("
                + "record_date, year_value, month_sin, month_cos, summer, winter,"
                + "monday, tuesday, wednesday, thursday, friday,"
                + "bus_id, stat_idx, morning, evening, temp, precip, snow, congestion)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                , "2024-12-02",0.24,-2.4492935982947E-16,1,0,1,1,0,0,0,0,9,1,1,0,0.636932707,0,0,0);
    }

    @Test
    public void successReadFile() throws Exception {
        reader.open(new ExecutionContext());
        FeaturedCongestionData data = reader.read();

        assertThat(data.getBusId()).isEqualTo(9);
    }
}