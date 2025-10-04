package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LatestTradesResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("trades")
    private final List<LatestTrade> trades;

}