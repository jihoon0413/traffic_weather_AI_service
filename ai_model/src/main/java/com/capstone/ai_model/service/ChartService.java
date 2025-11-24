package com.capstone.ai_model.service;

import com.capstone.ai_model.dto.EvaluationData;
import lombok.Getter;
import lombok.Setter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class ChartService {

    private INDArray realValues;
    private INDArray predValues;

    public void setData(INDArray realValues, INDArray predValues) {
        this.realValues = realValues;
        this.predValues = predValues;
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
