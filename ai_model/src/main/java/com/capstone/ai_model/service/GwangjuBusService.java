package com.capstone.ai_model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GwangjuBusService {

    private final WebClient webClient;

    @Value("${open-api.bis-key}")
    private String secretKey;


    public Mono<String> getRouteList(String ars_id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("api.gwangju.go.kr")
                        .path("/json/lineStationInfo")
                        .queryParam("serviceKey", secretKey)
                        .queryParam("LINE_ID", ars_id)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class);
    }

}
