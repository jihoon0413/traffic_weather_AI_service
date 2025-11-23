package com.capstone.ai_model.training.data;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import com.capstone.ai_model.dto.LSTMInput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SequenceBuilder {

    private static final int TIME_SERIES_LENGTH = 15;
    private static final int FEATURE_SIZE = 16;

    public List<LSTMInput> buildSequencesByStation(List<FeaturedCongestionData> data) {
        List<LSTMInput> result = new ArrayList<>();

        // 1) 정류장별 그룹핑
        Map<Long, List<FeaturedCongestionData>> byStation = data.stream()
                .collect(Collectors.groupingBy(d -> (long) d.getStat_idx()));

        // 2) 각 정류장별로 시간순 정렬 + 슬라이딩 윈도우
        for (Map.Entry<Long, List<FeaturedCongestionData>> entry : byStation.entrySet()) {
            List<FeaturedCongestionData> stationData = entry.getValue();

            stationData.sort(
                    Comparator.comparing(FeaturedCongestionData::getRecord_date)
                            .thenComparing(FeaturedCongestionData::getMorning).reversed()
            );

            List<FeaturedCongestionData> buffer = new ArrayList<>();

            for (FeaturedCongestionData item : stationData) {
                buffer.add(item);
                if (buffer.size() < TIME_SERIES_LENGTH) continue;

                result.add(makeLstmInput(buffer));
                buffer.remove(0);
            }
        }
        return result;
    }

    private LSTMInput makeLstmInput(List<FeaturedCongestionData> buffer) {

        int[][] stationIndex = new int[1][TIME_SERIES_LENGTH];
        double[][][] features = new double[1][FEATURE_SIZE][TIME_SERIES_LENGTH];
        double[][] label = new double[1][1];

        for (int t = 0; t < TIME_SERIES_LENGTH; t++) {
            FeaturedCongestionData d = buffer.get(t);

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
            features[0][10][t] = d.getBusId();
            features[0][11][t] = d.getMorning();
            features[0][12][t] = d.getEvening();
            features[0][13][t] = d.getTemp();
            features[0][14][t] = d.getPrecip();
            features[0][15][t] = d.getSnow();

            label[0][0] = d.getCongestion();
        }

        return new LSTMInput(
                Nd4j.createFromArray(stationIndex),
                Nd4j.create(features),
                Nd4j.create(label)
        );
    }
}
