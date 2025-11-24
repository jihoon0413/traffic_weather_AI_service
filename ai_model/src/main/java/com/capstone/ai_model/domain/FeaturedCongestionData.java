package com.capstone.ai_model.domain;

import com.capstone.ai_model.dto.FeatureData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedCongestionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String record_date;
    private double year_value;
    private double monthSin;
    private double monthCos;
    private double summer;
    private double winter;
    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
    private double stat_idx;
    private double morning;
    private double evening;
    private double temp;
    private double precip;
    private double snow;
    private double congestion;

    public static FeaturedCongestionData ofMorning(FeatureData data, DateTimeFormatter formatter) {
        double[] temp = data.getMorningFeature();

        return new FeaturedCongestionData(null, formatter.format(data.getDate()), temp[0], temp[1], temp[2], temp[3], temp[4], temp[5],
                temp[6], temp[7], temp[8], temp[9], temp[10], temp[11],
                temp[12], temp[13], temp[14], temp[15], data.getMorningCongestion());
    }

    public static FeaturedCongestionData ofEvening(FeatureData data, DateTimeFormatter formatter) {
        double[] temp = data.getEveningFeature();
        return new FeaturedCongestionData(null, formatter.format(data.getDate()), temp[0], temp[1], temp[2], temp[3], temp[4], temp[5],
                temp[6], temp[7], temp[8], temp[9], temp[10], temp[11],
                temp[12], temp[13], temp[14], temp[15], data.getEveningCongestion());
    }

}
