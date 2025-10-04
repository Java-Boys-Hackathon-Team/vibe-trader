package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class TransactionTrade {

    @JsonProperty("size")
    private final BigDecimalValueWrapper size;

    @JsonProperty("price")
    private final BigDecimalValueWrapper price;

    @JsonProperty("accrued_interest")
    private final BigDecimalValueWrapper accruedInterest;

}
