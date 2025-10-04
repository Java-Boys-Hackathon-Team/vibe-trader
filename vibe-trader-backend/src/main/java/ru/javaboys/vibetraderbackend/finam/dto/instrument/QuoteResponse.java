package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuoteResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("quote")
    private final Quote quote;

}