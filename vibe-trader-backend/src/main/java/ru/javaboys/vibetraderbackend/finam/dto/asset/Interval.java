package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Interval {

    @JsonProperty("start_time")
    private final String startTime;

    @JsonProperty("end_time")
    private final String endTime;

}