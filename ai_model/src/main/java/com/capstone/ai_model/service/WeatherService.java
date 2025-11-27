package com.capstone.ai_model.service;

import com.capstone.ai_model.domain.eNum.SearchTime;
import com.capstone.ai_model.dto.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient webClient;

    @Value("${open-api.weather-key}")
    private String weatherKey;

    public WeatherResponse getWeatherData(String date, double lat, double lng, SearchTime time) {

        String strat;
        String end;
        if(time == SearchTime.MORNING) {
            strat = date+"0700";
            end = date+"1000";
        } else {
            strat = date+"1700";
            end = date+"2000";
        }

        String weatherInfo = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("apihub.kma.go.kr")
                        .path("/api/typ01/url/sfc_nc_var.php")
                        .queryParam("tm1", strat)
                        .queryParam("tm2",end)
                        .queryParam("lat",lat)
                        .queryParam("lon",lng)
                        .queryParam("obs","ta_chi,rn_60m,sd_tot")
                        .queryParam("itv",180)
                        .queryParam("help",0)
                        .queryParam("authKey",weatherKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return getWeatherResponse(weatherInfo);
    }

    private WeatherResponse getWeatherResponse(String weatherInfo) {
        String[] values = weatherInfo.split("\n")[1].split(",");
        return WeatherResponse.of(values[0], values[1], values[2], values[3]);
    }

}
