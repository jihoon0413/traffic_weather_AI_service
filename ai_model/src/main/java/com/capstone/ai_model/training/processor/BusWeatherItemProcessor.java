package com.capstone.ai_model.training.processor;

import com.capstone.ai_model.dto.training.BusWeatherData;
import com.capstone.ai_model.dto.training.FeatureData;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.shade.jackson.core.JsonProcessingException;
import org.nd4j.shade.jackson.core.type.TypeReference;
import org.nd4j.shade.jackson.databind.ObjectMapper;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@JobScope
public class BusWeatherItemProcessor implements ItemProcessor<BusWeatherData, FeatureData> {

    private static final List<String> DAYS = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");
    private Map<Integer, Integer> statIdMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> preCongestion = new ConcurrentHashMap<>();

    private int maxCongestion;
    private double maxTemp;
    private double minTemp;
    private double maxPrecip;
    private double maxSnow;

    @Override
    public FeatureData process(BusWeatherData item) throws Exception {

        String morningKey = buildKey(item)+"morning";
        String eveningKey = buildKey(item)+"evening";
        int featureSize = 5 + 5 + 1 + 1 + 2 + 3; //년월일, 요일, 버스ID, 정류장ID, 출퇴근시간, 온도, 1시간 강수량, 적설량


        double[] morningFeature = new double[featureSize];
        double[] eveningFeature = new double[featureSize];
        int idx = 0;

        LocalDate localDate = item.getDate();

        // 년, 월(sin, cos), 계절(여름, 겨울)
        double[] dateEmbed = normalizeDate(localDate);
        System.arraycopy(dateEmbed, 0, morningFeature, idx, dateEmbed.length);
        System.arraycopy(dateEmbed, 0, eveningFeature, idx, dateEmbed.length);
        idx += dateEmbed.length;

        double[] oneHot = encodeDay(localDate.getDayOfWeek().toString());
        System.arraycopy(oneHot, 0, morningFeature, idx, oneHot.length);
        System.arraycopy(oneHot, 0, eveningFeature, idx, oneHot.length);
        idx += oneHot.length;

        // 버스Id 추후에 여러개의 버스 학습시 임베딩을 위한 매핑 과정 필요
        morningFeature[idx] = item.getBusId();
        eveningFeature[idx] = item.getBusId();
        idx++;

        // statId
        int statIndex = item.getSeq(); // 정류장의 순서가 사실장 index역할 추가적인 매핑이 필요 없음
        morningFeature[idx] = statIndex;
        eveningFeature[idx] = statIndex;
        idx++;

        // morning,evening
        morningFeature[idx] = 1;  // 아침 시간대일 때
        eveningFeature[idx] = 0;
        idx++;
        morningFeature[idx] = 0; // 저녁 시간대일 때
        eveningFeature[idx] = 1;
        idx++;

        // 기상 데이터
        morningFeature[idx] = (item.getMorning_avg_temp_c() - minTemp)/(maxTemp - minTemp);
        eveningFeature[idx] = (item.getEvening_avg_temp_c() - minTemp)/(maxTemp - minTemp);
        idx++;

        if(maxPrecip == 0) {
            morningFeature[idx] = 0;
            eveningFeature[idx] = 0;
        } else {
            morningFeature[idx] = (item.getMorning_avg_precip_mm() - 0)/(maxPrecip - 0);
            eveningFeature[idx] = (item.getEvening_avg_precip_mm() - 0)/(maxPrecip - 0);
        }
        idx++;

        morningFeature[idx] = (item.getMorning_avg_snow_cm() - 0)/(maxSnow - 0);
        eveningFeature[idx] = (item.getEvening_avg_snow_cm() - 0)/(maxSnow - 0);

        // 결과값
        int morningCongestion = preCongestion.getOrDefault(morningKey, 0);
        int eveningCongestion = preCongestion.getOrDefault(eveningKey, 0);



        preCongestion.put(morningKey, morningCongestion + item.getCommuteOnPassengers() - item.getCommuteOffPassengers());
        preCongestion.put(eveningKey, eveningCongestion + item.getOffPeakOnPassengers() - item.getOffPeakOffPassengers());

        double normalizedMorningCongestion = (double) morningCongestion /maxCongestion;
        double normalizedEveningCongestion = (double) eveningCongestion /maxCongestion;

//        log.info("{} : {}, {} : {} ", morningKey, morningCongestion, eveningKey, eveningCongestion);

        return new FeatureData(item.getDate(), morningFeature, normalizedMorningCongestion, eveningFeature, normalizedEveningCongestion);
    }

    @BeforeStep
    public void loadStatus(StepExecution stepExecution) throws JsonProcessingException {
        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
        String json = context.getString("statIdMapJson");
        this.statIdMap = new ObjectMapper().readValue(json, new TypeReference<>(){});
        this.maxCongestion = context.getInt("maxCongestion");
        this.maxTemp = context.getDouble("maxTemp");
        this.minTemp = context.getDouble("minTemp");
        this.maxPrecip = context.getDouble("maxPrecip");
        this.maxSnow = context.getDouble("maxSnow");

//        log.info("{}  ,  {}  ,  {}  ,  {}  ,  {}  ,  {}" , maxCongestion, minCongestion, maxTemp, minTemp, maxPrecip, maxSnow);
    }


    private String buildKey(BusWeatherData data) {
        return data.getDate() + "_" + data.getBusId() + "_";
    }

    private double[] normalizeDate(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        double yearNormal = (year - 2000) / 100.0;
        double monthSin = Math.sin(2 * Math.PI * month / 12.0);
        double monthCos = Math.cos(2 * Math.PI * month / 12.0);
        double seasonSummer = (month >= 6 && month <= 8) ? 1.0 : 0.0;
        double seasonWinter = (month == 12 || month == 1 || month == 2) ? 1.0 : 0.0;

        double[] normalizedDate = new double[5];

        normalizedDate[0] = yearNormal;
        normalizedDate[1] = monthSin;
        normalizedDate[2] = monthCos;
        normalizedDate[3] = seasonSummer;
        normalizedDate[4] = seasonWinter;

        return normalizedDate;
    }

    private double[] encodeDay(String day) {
        double[] oneHot = new double[5];
        int idx = DAYS.indexOf(day);
        if (idx >= 0) {
            oneHot[idx] = 1.0;
        }
        return oneHot;
    }
}
