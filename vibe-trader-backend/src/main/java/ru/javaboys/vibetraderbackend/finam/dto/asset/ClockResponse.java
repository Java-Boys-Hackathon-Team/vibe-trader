package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ClockResponse {

    @JsonProperty("timestamp")
    private final Instant timestamp;

}
