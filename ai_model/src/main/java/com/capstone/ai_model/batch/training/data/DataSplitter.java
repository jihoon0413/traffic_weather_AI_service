package com.capstone.ai_model.batch.training.data;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import com.capstone.ai_model.dto.TrainTestSplit;
import java.util.List;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSplitter {

    public TrainTestSplit<FeaturedCongestionData> splitSeasonByTime(List<FeaturedCongestionData> seasonList) {
        int total = seasonList.size();
        if (total == 0) {
            throw new IllegalStateException("시즌 데이터가 비어 있습니다.");
        }

        int trainSize = (int) (total * 0.8);

        List<FeaturedCongestionData> train = seasonList.subList(0, trainSize);
        List<FeaturedCongestionData> test  = seasonList.subList(trainSize, total);

        return new TrainTestSplit<>(train, test);
    }
}
