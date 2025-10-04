package ru.javaboys.vibetraderbackend.finam.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TradesResponse {

    @JsonProperty("trades")
    private final List<Trade> trades;

}