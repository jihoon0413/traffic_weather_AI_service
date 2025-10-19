package com.capstone.ai_model.dto;

import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeaturedCongestionData {
    private String date;
    private double year;
    private double monthSin;
    private double monthCos;
    private double summer;
    private double winter;
    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
    private double busId;
    private double statIdx;
    private double morning;
    private double evening;
    private double temp;
    private double precip;
    private double snow;
    private double congestion;

    public static FeaturedCongestionData ofMorning(FeatureData data) {
        double[] temp = data.getMorningFeature();
        DateTimeFormatter formatter = getFormatter();

        return new FeaturedCongestionData(formatter.format(data.getDate()), temp[0], temp[1], temp[2], temp[3], temp[4], temp[5],
                temp[6], temp[7], temp[8], temp[9], temp[10], temp[11],
                temp[12], temp[13], temp[14], temp[15], temp[16], data.getMorningCongestion());
    }

    public static FeaturedCongestionData ofEvening(FeatureData data) {
        double[] temp = data.getEveningFeature();
        DateTimeFormatter formatter = getFormatter();
        return new FeaturedCongestionData(formatter.format(data.getDate()), temp[0], temp[1], temp[2], temp[3], temp[4], temp[5],
                temp[6], temp[7], temp[8], temp[9], temp[10], temp[11],
                temp[12], temp[13], temp[14], temp[15], temp[16], data.getEveningCongestion());
    }

    private static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }

}
