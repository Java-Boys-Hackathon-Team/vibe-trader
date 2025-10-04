package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LatestTradesResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("trades")
    private final List<LatestTrade> trades;

}