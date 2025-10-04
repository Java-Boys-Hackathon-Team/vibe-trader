package ru.javaboys.vibetraderbackend.finam.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class Trade {

    @JsonProperty("trade_id")
    private final String tradeId;

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("price")
    private final BigDecimalValueWrapper price;

    @JsonProperty("size")
    private final BigDecimalValueWrapper size;

    @JsonProperty("side")
    private final String side;

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("order_id")
    private final String orderId;

    @JsonProperty("account_id")
    private final String accountId;

}
