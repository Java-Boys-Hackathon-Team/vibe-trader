package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class OrderLeg {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("quantity")
    private final BigDecimalValueWrapper quantity;

    @JsonProperty("side")
    private final String side;

}
