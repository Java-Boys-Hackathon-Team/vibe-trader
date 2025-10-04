package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class OrderBookRow {

    @JsonProperty("price")
    private final BigDecimalValueWrapper price;

    @JsonProperty("sell_size")
    private final BigDecimalValueWrapper sellSize;

    @JsonProperty("action")
    private final String action;

    @JsonProperty("mpid")
    private final String mpid;

    @JsonProperty("timestamp")
    private final String timestamp;
}