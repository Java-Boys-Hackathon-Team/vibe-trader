package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CloseOrderResponse {

    @JsonProperty("order_id")
    private final String orderId;

    @JsonProperty("exec_id")
    private final String execId;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("order")
    private final Order order;

    @JsonProperty("transact_at")
    private final String transactAt;

}