package com.capstone.ai_model.service;

import com.capstone.ai_model.dto.EvaluationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class ChartService {

    private INDArray predValues;
    private INDArray realValues;

    public void setData() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(new File("prediction_data.json"), Map.class);

        double[] pred = ((List<Double>) map.get("pred")).stream().mapToDouble(d -> d).toArray();
        double[] real = ((List<Double>) map.get("real")).stream().mapToDouble(d -> d).toArray();

        this.predValues = Nd4j.create(pred);
        this.realValues = Nd4j.create(real);
    }

    public EvaluationData getEvaluation() {
        INDArray diff = predValues.sub(realValues).dup();

        // MAE
        INDArray absDiff = Transforms.abs(diff);
        double mae = absDiff.meanNumber().doubleValue();

        // MSE / RMSE
        INDArray sq = diff.mul(diff);
        double mse = sq.meanNumber().doubleValue();
        double rmse = Math.sqrt(mse);

        // sMAPE
        double eps = 1e-6;
        INDArray denominator = Transforms.abs(predValues).add(Transforms.abs(realValues)).add(eps);
        INDArray smapeArray = absDiff.mul(2).div(denominator);
        double smape = smapeArray.meanNumber().doubleValue() * 100;

        // R²
        double meanReal = realValues.meanNumber().doubleValue();
        INDArray centered = realValues.sub(meanReal);
        double ssTot = centered.mul(centered).sumNumber().doubleValue();
        double ssRes = sq.sumNumber().doubleValue();
        double r2 = 1.0 - ssRes / ssTot;

        return new EvaluationData(mae, mse, rmse, smape, r2);

    }
}
