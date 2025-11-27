package com.capstone.ai_model.dto.response;

public record WeatherResponse(
        String date,
        String temp,
        String prec,
        String snow
) {
    public static WeatherResponse of (String date, String temp, String prec, String snow) {
        return new WeatherResponse(date, temp, prec, snow);
    }

}
