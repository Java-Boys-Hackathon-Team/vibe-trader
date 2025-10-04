package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class Quote {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("ask")
    private final BigDecimalValueWrapper ask;

    @JsonProperty("ask_size")
    private final BigDecimalValueWrapper askSize;

    @JsonProperty("bid")
    private final BigDecimalValueWrapper bid;

    @JsonProperty("bid_size")
    private final BigDecimalValueWrapper bidSize;

    @JsonProperty("last")
    private final BigDecimalValueWrapper last;

    @JsonProperty("last_size")
    private final BigDecimalValueWrapper lastSize;

    @JsonProperty("volume")
    private final BigDecimalValueWrapper volume;

    @JsonProperty("turnover")
    private final BigDecimalValueWrapper turnover;

    @JsonProperty("open")
    private final BigDecimalValueWrapper open;

    @JsonProperty("high")
    private final BigDecimalValueWrapper high;

    @JsonProperty("low")
    private final BigDecimalValueWrapper low;

    @JsonProperty("close")
    private final BigDecimalValueWrapper close;

    @JsonProperty("change")
    private final BigDecimalValueWrapper change;

}