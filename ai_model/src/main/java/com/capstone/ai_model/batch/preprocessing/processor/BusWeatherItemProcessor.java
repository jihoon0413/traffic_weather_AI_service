package com.capstone.ai_model.batch.preprocessing.processor;

import com.capstone.ai_model.dto.BusWeatherData;
import com.capstone.ai_model.dto.FeatureData;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.shade.jackson.core.JsonProcessingException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobScope
public class BusWeatherItemProcessor implements ItemProcessor<BusWeatherData, FeatureData> {

    private static final List<String> DAYS = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");
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
        int featureSize = 5 + 5 + 1 + 2 + 3; //년월일, 요일, 버스ID, 정류장ID, 출퇴근시간, 온도, 1시간 강수량, 적설량


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
//        morningFeature[idx] = item.getBusId();
//        eveningFeature[idx] = item.getBusId();
//        idx++;

        // statId
        int statIndex = item.getSeq();
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

        double correction = 1 / (1 - 0.43); //하차 비율 보정
        double newCommuteOffPassengers = item.getCommuteOffPassengers()* correction;
        double newOffPeakPassengers = item.getOffPeakOffPassengers()* correction;

        int morningCongestion = (int) (preCongestion.getOrDefault(morningKey, 0)+ item.getCommuteOnPassengers() - newCommuteOffPassengers);
        int eveningCongestion = (int) (preCongestion.getOrDefault(eveningKey, 0)+ item.getOffPeakOnPassengers() - newOffPeakPassengers);

        preCongestion.put(morningKey, morningCongestion);
        preCongestion.put(eveningKey, eveningCongestion);

        double normalizedMorningCongestion = (double) morningCongestion /maxCongestion;
        double normalizedEveningCongestion = (double) eveningCongestion /maxCongestion;

        log.info("{} : {}, {} : {} ", morningKey, morningCongestion, eveningKey, eveningCongestion);

        return new FeatureData(item.getDate(), morningFeature, normalizedMorningCongestion, eveningFeature, normalizedEveningCongestion);
    }

    @BeforeStep
    public void loadStatus(StepExecution stepExecution) throws JsonProcessingException {
        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
        this.maxCongestion = context.getInt("maxCongestion");
        this.maxTemp = context.getDouble("maxTemp");
        this.minTemp = context.getDouble("minTemp");
        this.maxPrecip = context.getDouble("maxPrecip");
        this.maxSnow = context.getDouble("maxSnow");
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
