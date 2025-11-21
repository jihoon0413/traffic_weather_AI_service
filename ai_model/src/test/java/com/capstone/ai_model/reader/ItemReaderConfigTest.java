package com.capstone.ai_model.reader;

import com.capstone.ai_model.dto.training.BusWeatherData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBatchTest
class ItemReaderConfigTest {

    @Autowired
    private FlatFileItemReader<BusWeatherData> reader;

    @Test
    public void successReadFile() throws Exception {
        reader.open(new ExecutionContext());

        BusWeatherData item = reader.read();

        assertThat(item).isNotNull();
        assertThat(item.getBusStatId()).isEqualTo(1165);
        assertThat(item.getDate()).isEqualTo("20241202");
        assertThat(item.getBusName()).isEqualTo("문흥18");
        assertThat(item.getBusStatName()).isEqualTo("장등동");
        assertThat(item.getMorning_avg_temp_c()).isEqualTo(4.75);

        reader.close();
    }

}