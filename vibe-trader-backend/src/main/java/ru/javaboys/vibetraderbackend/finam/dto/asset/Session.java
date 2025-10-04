package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Session {

    @JsonProperty("type")
    private final String type;

    @JsonProperty("interval")
    private final Interval interval;

}