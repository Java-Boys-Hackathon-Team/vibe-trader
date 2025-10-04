package ru.javaboys.vibetraderbackend.finam.dto.asset;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClockResponse {

    @JsonProperty("timestamp")
    private final Instant timestamp;

}
