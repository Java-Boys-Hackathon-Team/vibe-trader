package ru.javaboys.vibetraderbackend.finam.dto.account;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class PlaceOrderRequest {

    @JsonProperty("account_id")
    private final String accountId;

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("quantity")
    private final BigDecimalValueWrapper quantity;

    @JsonProperty("side")
    private final String side;// todo make enum?

    @JsonProperty("type")
    private final OrderType type;

    @JsonProperty("time_in_force")
    private final TimeInForceType timeInForce;

    @JsonProperty("limit_price")
    private final BigDecimalValueWrapper limitPrice;

    @JsonProperty("stop_price")
    private final BigDecimalValueWrapper stopPrice;

    @JsonProperty("stop_condition")
    private final StopConditionType stopCondition;

    @JsonProperty("legs")
    private final List<OrderLeg> legs;

    @Size(min = 1, max = 20)
    @JsonProperty("client_order_id")
    private final String clientOrderId;

    @JsonProperty("valid_before")
    private final ValidBeforeType validBefore;

    @JsonProperty("comment")
    private final String comment;

}