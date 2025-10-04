package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Asset {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("ticker")
    private final String ticker;

    @JsonProperty("mic")
    private final String mic;

    @JsonProperty("isin")
    private final String isin;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("name")
    private final String name;

}