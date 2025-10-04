package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;
import ru.javaboys.vibetraderbackend.finam.dto.DateValue;

@Getter
@Builder
public class AssetOption {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("contract_size")
    private final BigDecimalValueWrapper contractSize;

    @JsonProperty("trade_last_day")
    private final DateValue tradeLastDay;

    @JsonProperty("strike")
    private final BigDecimalValueWrapper strike;

    @JsonProperty("expiration_first_day")
    private final DateValue expirationFirstDay;

    @JsonProperty("expiration_last_day")
    private final DateValue expirationLastDay;

}