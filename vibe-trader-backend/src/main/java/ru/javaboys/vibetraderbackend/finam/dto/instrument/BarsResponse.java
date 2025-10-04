package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BarsResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("bars")
    private final List<Bar> bars;

}