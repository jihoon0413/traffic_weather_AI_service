package com.capstone.ai_model.training.pipeline;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import com.capstone.ai_model.dto.LSTMInput;
import com.capstone.ai_model.dto.TrainTestSplit;
import com.capstone.ai_model.training.data.DataLoader;
import com.capstone.ai_model.training.data.DataSplitter;
import com.capstone.ai_model.training.data.SequenceBuilder;
import com.capstone.ai_model.training.model.ModelTrainer;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LstmTrainingTasklet implements Tasklet {

    private final DataSource dataSource;
    private final DataLoader dbLoader;
    private final DataSplitter dataSplitter;
    private final SequenceBuilder sequenceBuilder;
    private final ModelTrainer modelTrainer;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        // 1) 시즌별 전체 데이터 로딩
        List<FeaturedCongestionData> summer = dbLoader.loadSeasonData(jdbc, true);
        List<FeaturedCongestionData> winter = dbLoader.loadSeasonData(jdbc, false);

        // 2) 각 시즌별 Train/Test Split (시간 기반 80/20)
        TrainTestSplit<FeaturedCongestionData> summerSplit = dataSplitter.splitSeasonByTime(summer);
        TrainTestSplit<FeaturedCongestionData> winterSplit = dataSplitter.splitSeasonByTime(winter);

//         3) LSTMInput 시퀀스로 변환 (슬라이딩 윈도우)
//        List<LSTMInput> trainSummer = sequenceBuilder.buildSequencesByStation(summer);
        List<LSTMInput> trainSummer = sequenceBuilder.buildSequencesByStation(summerSplit.getTrain());
        List<LSTMInput> testSummer  = sequenceBuilder.buildSequencesByStation(summerSplit.getTest());
        List<LSTMInput> trainWinter = sequenceBuilder.buildSequencesByStation(winter);
//        List<LSTMInput> trainWinter = sequenceBuilder.buildSequencesByStation(winterSplit.getTrain());
        List<LSTMInput> testWinter  = sequenceBuilder.buildSequencesByStation(winterSplit.getTest());


        // 4) DL4J 모델 학습 및 시즌별 평가
        modelTrainer.trainAndEvaluate(trainSummer, testSummer, trainWinter, testWinter);


        return RepeatStatus.FINISHED;
    }
}