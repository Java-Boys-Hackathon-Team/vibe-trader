package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class LatestTrade {

    @JsonProperty("trade_id")
    private final String tradeId;

    @JsonProperty("mpid")
    private final String mpid;

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("price")
    private final BigDecimalValueWrapper price;

    @JsonProperty("size")
    private final BigDecimalValueWrapper size;

    @JsonProperty("side")
    private final String side;

}