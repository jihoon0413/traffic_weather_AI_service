package com.capstone.ai_model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusWeatherData {
    private int busStatId;
    private int seq;
    private String busStopName;
    private int busId;
    private String busName;
    private LocalDate date;
    private int commuteOnPassengers;
    private int offPeakOnPassengers;
    private int commuteOffPassengers;
    private int offPeakOffPassengers;
    private double morning_avg_temp_c;
    private double morning_avg_precip_mm;
    private double morning_avg_snow_cm;
    private double evening_avg_temp_c;
    private double evening_avg_precip_mm;
    private double evening_avg_snow_cm;

}
