package com.capstone.ai_model.service;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import com.capstone.ai_model.domain.ModelMetaData;
import com.capstone.ai_model.domain.eNum.SearchTime;
import com.capstone.ai_model.dto.LSTMInput;
import com.capstone.ai_model.repository.FeaturedDataRepository;
import com.capstone.ai_model.repository.ModelMetaDataRepository;
import com.capstone.ai_model.repository.StatMappingRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictService {

    private static final List<String> DAYS = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");
    static final int TIME_SERIES_LENGTH = 15;   // 네가 학습에 사용한 값
    static final int FEATURE_SIZE = 15;

    private final FeaturedDataRepository featuredDataRepository;
    private final ModelMetaDataRepository modelMetaDataRepository;
    private final StatMappingRepository statMappingRepository;
    private final DateTimeFormatter dateFormatter;

    @Setter
    private ComputationGraph model;

    public double predictCongestion(String date, int stopId, double temp, double prec, double snow, SearchTime time) {

        long statIndex = statMappingRepository.findById(stopId).orElseThrow().getEmbeddingIndex();
        ModelMetaData metaData = modelMetaDataRepository.findById(1L).orElseThrow();

        double[] feature = getFeature(date, statIndex, temp, prec, snow, time, metaData);

        List<FeaturedCongestionData> seq = featuredDataRepository.findRecentSequence(statIndex, PageRequest.of(0, TIME_SERIES_LENGTH-1));
        Collections.reverse(seq);

        FeaturedCongestionData inputDate = new FeaturedCongestionData(null, date, feature[0], feature[1], feature[2], feature[3], feature[4], feature[5],
                feature[6], feature[7], feature[8], feature[9], feature[10], feature[11],
                feature[12], feature[13], feature[14], feature[15], 0);

        seq.add(inputDate);

        LSTMInput input = buildLstmInput(seq);

        INDArray stationArr = input.getStatIdx();
        INDArray featureArr = input.getFeatures();

        INDArray[] output = model.output(new INDArray[]{stationArr, featureArr});

        double y_pred = output[0].getDouble(0, 0, TIME_SERIES_LENGTH - 1);
        double predictResult = Math.round(((y_pred * metaData.getMaxCongestion())/16)*100)/100.0;
        return predictResult < 0 ? 0.0 : predictResult;
    }

    private LSTMInput buildLstmInput(List<FeaturedCongestionData> seq) {

        int[][] stationIndex = new int[1][TIME_SERIES_LENGTH];
        double[][][] features = new double[1][FEATURE_SIZE][TIME_SERIES_LENGTH];

        for (int t = 0; t < TIME_SERIES_LENGTH; t++) {
            FeaturedCongestionData d = seq.get(t);
            stationIndex[0][t] = (int) d.getStat_idx();

            features[0][0][t] = d.getYear_value();
            features[0][1][t] = d.getMonthSin();
            features[0][2][t] = d.getMonthCos();
            features[0][3][t] = d.getSummer();
            features[0][4][t] = d.getWinter();
            features[0][5][t] = d.getMonday();
            features[0][6][t] = d.getTuesday();
            features[0][7][t] = d.getWednesday();
            features[0][8][t] = d.getThursday();
            features[0][9][t] = d.getFriday();
            features[0][10][t] = d.getMorning();
            features[0][11][t] = d.getEvening();
            features[0][12][t] = d.getTemp();
            features[0][13][t] = d.getPrecip();
            features[0][14][t] = d.getSnow();
        }

        return new LSTMInput(
                Nd4j.createFromArray(stationIndex),   // shape: [1, TIME_SERIES_LENGTH]
                Nd4j.create(features),                // shape: [1, FEATURE_SIZE, TIME_SERIES_LENGTH
                null
        );
    }


    private double[] getFeature(String date, long stopId, double temp, double prec, double snow, SearchTime time, ModelMetaData metaData) {

        double[] feature = new double[FEATURE_SIZE+1];
        int idx = 0;

        LocalDate localDate = LocalDate.parse(date, dateFormatter);

        double[] dateEmbed = normalizeDate(localDate);
        System.arraycopy(dateEmbed, 0, feature, idx, dateEmbed.length);
        idx += dateEmbed.length;

        double[] oneHot = encodeDay(localDate.getDayOfWeek().toString());
        System.arraycopy(oneHot, 0, feature, idx, oneHot.length);
        idx += oneHot.length;

        feature[idx] = (double) stopId;
        idx++;

        if(time == SearchTime.MORNING) {
            feature[idx] = 1;
            idx++;
            feature[idx] = 0;
            idx++;
        } else {
            feature[idx] = 0;
            idx++;
            feature[idx] = 1;
            idx++;
        }

        feature[idx] = (temp - metaData.getMinTemp())/(metaData.getMaxTemp()-metaData.getMinTemp());
        idx++;
        feature[idx] = (prec - 0)/(metaData.getMaxPrecip()-0);
        idx++;
        feature[idx] = (snow - 0)/(metaData.getMaxSnow()-0);

        return feature;
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
