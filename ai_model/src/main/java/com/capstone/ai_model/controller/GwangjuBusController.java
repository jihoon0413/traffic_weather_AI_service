package com.capstone.ai_model.controller;

import com.capstone.ai_model.service.GwangjuBusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GwangjuBusController {

    private final GwangjuBusService gwangjuBusService;

    @GetMapping("/api/station/{ars_id}")
    public Mono<String> getStation(@PathVariable("ars_id") String id) {
        return gwangjuBusService.getRouteList(id);
    }

}
