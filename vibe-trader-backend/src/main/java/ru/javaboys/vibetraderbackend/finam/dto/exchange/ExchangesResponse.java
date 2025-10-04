package ru.javaboys.vibetraderbackend.finam.dto.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExchangesResponse {

    @JsonProperty("exchanges")
    private final List<Exchange> exchanges;

}