package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

import java.util.List;

@Getter
@Builder
public class Order {

    @JsonProperty("account_id")
    private final String accountId;

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("quantity")
    private final BigDecimalValueWrapper quantity;

    @JsonProperty("side")
    private final String side;

    @JsonProperty("type")
    private final OrderType type;

    @JsonProperty("time_in_force")
    private final String timeInForce;

    @JsonProperty("limit_price")
    private final BigDecimalValueWrapper limitPrice;

    @JsonProperty("stop_condition")
    private final String stopCondition;

    @JsonProperty("legs")
    private final List<Object> legs;

    @JsonProperty("client_order_id")
    private final String clientOrderId;

    @JsonProperty("valid_before")
    private final ValidBeforeType validBefore;

}