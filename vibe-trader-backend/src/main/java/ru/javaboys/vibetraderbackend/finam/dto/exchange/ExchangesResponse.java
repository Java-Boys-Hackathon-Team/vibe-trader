package ru.javaboys.vibetraderbackend.finam.dto.exchange;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangesResponse {

    @JsonProperty("exchanges")
    private final List<Exchange> exchanges;

}