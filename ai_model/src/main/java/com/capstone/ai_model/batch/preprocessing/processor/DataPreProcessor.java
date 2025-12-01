package com.capstone.ai_model.batch.preprocessing.processor;

import com.capstone.ai_model.domain.ModelMetaData;
import com.capstone.ai_model.dto.BusWeatherData;
import com.capstone.ai_model.repository.ModelMetaDataRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.shade.jackson.core.JsonProcessingException;
import org.nd4j.shade.jackson.databind.ObjectMapper;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@JobScope
@Component
@RequiredArgsConstructor
public class DataPreProcessor implements ItemProcessor<BusWeatherData, BusWeatherData> {

    private final Map<Integer, Integer> statIdMap = new ConcurrentHashMap<>();
    private final ModelMetaDataRepository modelMetaDataRepository;

    private final Map<String, Integer> preCongestion = new ConcurrentHashMap<>();

    private int maxCongestion = Integer.MIN_VALUE;

    private double maxTemp = Integer.MIN_VALUE;
    private double minTemp = Integer.MAX_VALUE;

    private double maxPrecip = Integer.MIN_VALUE;

    private double maxSnow = Integer.MIN_VALUE;


    @Override
    public BusWeatherData process(BusWeatherData item) throws Exception {

        // 혼잡도 min, max
        String morningKey = buildKey(item)+"morning";
        String eveningKey = buildKey(item)+"evening";

        double correction = 1 / (1 - 0.57);
        double newCommuteOffPassengers = item.getCommuteOffPassengers()* correction;
        double newOffPeakPassengers = item.getOffPeakOffPassengers()* correction;

        int morningRawCongestion = preCongestion.getOrDefault(morningKey, 0);
        int eveningRawCongestion = preCongestion.getOrDefault(eveningKey, 0);

        morningRawCongestion = Math.max(morningRawCongestion, 0);
        eveningRawCongestion = Math.max(eveningRawCongestion, 0);

        int morningCongestion = (int) (morningRawCongestion - newCommuteOffPassengers + item.getCommuteOnPassengers());
        int eveningCongestion = (int) (eveningRawCongestion - newOffPeakPassengers + item.getOffPeakOnPassengers());


//        log.info("morningCongestion {} : {}", morningKey , morningCongestion);
        preCongestion.put(morningKey, morningCongestion);
        preCongestion.put(eveningKey, eveningCongestion);

        maxCongestion = Math.max(maxCongestion, Math.max(morningCongestion, eveningCongestion));

        //날씨 데이터 min, max
        maxTemp = Math.max(maxTemp, Math.max(item.getMorning_avg_temp_c(), item.getEvening_avg_temp_c()));
        minTemp = Math.min(minTemp, Math.min(item.getMorning_avg_temp_c(), item.getEvening_avg_temp_c()));

        maxPrecip = Math.max(maxPrecip, Math.max(item.getMorning_avg_precip_mm(), item.getEvening_avg_precip_mm()));

        maxSnow = Math.max(maxSnow, Math.max(item.getMorning_avg_snow_cm(), item.getEvening_avg_snow_cm()));

        return item;
    }

    @AfterStep
    public ExitStatus saveStatus(StepExecution stepExecution) throws JsonProcessingException {
        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
        String json = new ObjectMapper().writeValueAsString(statIdMap);

        context.putString("statIdMapJson", json);
        context.putInt("maxCongestion", maxCongestion);
        context.putDouble("maxTemp", maxTemp);
        context.putDouble("minTemp", minTemp);
        context.putDouble("maxPrecip", maxPrecip);
        context.putDouble("maxSnow", maxSnow);

        ModelMetaData data = new ModelMetaData(null, maxCongestion, maxTemp, minTemp, maxPrecip, maxSnow);
        modelMetaDataRepository.save(data);

        return ExitStatus.COMPLETED;
    }

    private String buildKey(BusWeatherData data) {
        return data.getDate() + "_" + data.getBusId() + "_";
    }
}
