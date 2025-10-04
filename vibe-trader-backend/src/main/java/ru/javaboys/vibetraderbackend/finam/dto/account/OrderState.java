package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderState {

    @JsonProperty("order_id")
    private final String orderId;

    @JsonProperty("exec_id")
    private final String execId;

    @JsonProperty("status")
    private final OrderStatusType status;

    @JsonProperty("order")
    private final Order order;

    @JsonProperty("transact_at")
    private final String transactAt;

    @JsonProperty("accept_at")
    private final String acceptAt;

    @JsonProperty("withdraw_at")
    private final String withdrawAt;

}
