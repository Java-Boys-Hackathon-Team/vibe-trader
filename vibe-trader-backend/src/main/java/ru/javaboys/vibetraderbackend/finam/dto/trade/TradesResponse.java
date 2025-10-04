package ru.javaboys.vibetraderbackend.finam.dto.trade;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TradesResponse {

    @JsonProperty("trades")
    private final List<Trade> trades;

}