package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BarsResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("bars")
    private final List<Bar> bars;

}