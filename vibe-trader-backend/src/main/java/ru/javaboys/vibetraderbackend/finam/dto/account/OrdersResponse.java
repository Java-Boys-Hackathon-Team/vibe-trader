package ru.javaboys.vibetraderbackend.finam.dto.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrdersResponse {

    @JsonProperty("orders")
    private final List<OrderState> orders;

}
