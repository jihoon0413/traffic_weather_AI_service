package com.capstone.ai_model.training.processor;

import com.capstone.ai_model.dto.training.FeaturedCongestionData;
import com.capstone.ai_model.dto.training.LSTMInput;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@JobScope
@Component
public class LstmDataProcessor implements ItemProcessor<FeaturedCongestionData, LSTMInput> {

    private static final int TIME_SERIES_LENGTH = 15;
    private static final int FEATURE_SIZE = 16;

    // 정류장별 버퍼 (각 정류장마다 독립 시계열)
    private final Map<Integer, List<FeaturedCongestionData>> stationBuffers = new HashMap<>();
    private final LinkedList<FeaturedCongestionData> buffer = new LinkedList<>();

    @Override
    public LSTMInput process(FeaturedCongestionData item) throws Exception {

        buffer.add(item);

        if(buffer.size() < TIME_SERIES_LENGTH) {
            return null;
        }

        int[][] stationIndex = new int[1][TIME_SERIES_LENGTH];
        double[][][] featureArray = new double[1][FEATURE_SIZE][TIME_SERIES_LENGTH];
        double[][] labelArray = new double[1][1];
//        double[][][] labelArray = new double[1][1][TIME_SERIES_LENGTH];

        for (int t = 0; t < TIME_SERIES_LENGTH ; t++) {
            FeaturedCongestionData d = buffer.get(t);

            stationIndex[0][t] = (int) d.getStat_idx();

            featureArray[0][0][t] = d.getYear_value();
            featureArray[0][1][t] = d.getMonthSin();
            featureArray[0][2][t] = d.getMonthCos();
            featureArray[0][3][t] = d.getSummer();
            featureArray[0][4][t] = d.getWinter();
            featureArray[0][5][t] = d.getMonday();
            featureArray[0][6][t] = d.getTuesday();
            featureArray[0][7][t] = d.getWednesday();
            featureArray[0][8][t] = d.getThursday();
            featureArray[0][9][t] = d.getFriday();
            featureArray[0][10][t] = d.getBusId();
            featureArray[0][11][t] = d.getMorning();
            featureArray[0][12][t] = d.getEvening();
            featureArray[0][13][t] = d.getTemp();
            featureArray[0][14][t] = d.getPrecip();
            featureArray[0][15][t] = d.getSnow();

            labelArray[0][0] = d.getCongestion();
//            labelArray[0][0][t] = d.getCongestion();
        }

        INDArray statIdx = Nd4j.createFromArray(stationIndex);
        INDArray features = Nd4j.create(featureArray);
        INDArray targets = Nd4j.create(labelArray);

        buffer.remove(0);

        return new LSTMInput(statIdx, features, targets);
    }
}
