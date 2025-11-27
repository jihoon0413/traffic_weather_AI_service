package com.capstone.ai_model.controller;

import com.capstone.ai_model.domain.eNum.SearchTime;
import com.capstone.ai_model.dto.response.WeatherResponse;
import com.capstone.ai_model.service.GwangjuBusService;
import com.capstone.ai_model.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ExternalApiController {

    private final GwangjuBusService gwangjuBusService;
    private final WeatherService weatherService;

    @GetMapping("/station/{ars_id}")
    public Mono<String> getStation(@PathVariable("ars_id") String id) {
        return gwangjuBusService.getRouteList(id);
    }

    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam("date") String date,
                                   @RequestParam("lat") double lat,
                                   @RequestParam("lng") double lng,
                                   @RequestParam("time") SearchTime time) {
        return weatherService.getWeatherData(date,lat,lng,time);
    }


}
