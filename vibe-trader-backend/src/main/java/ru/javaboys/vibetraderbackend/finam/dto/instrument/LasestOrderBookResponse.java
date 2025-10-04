package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LasestOrderBookResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("orderbook")
    private final OrderBook orderBook;

}