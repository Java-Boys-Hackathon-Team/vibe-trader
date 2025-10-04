package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class Position {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("quantity")
    private final BigDecimalValueWrapper quantity;

    @JsonProperty("average_price")
    private final BigDecimalValueWrapper averagePrice;

    @JsonProperty("current_price")
    private final BigDecimalValueWrapper currentPrice;

    @JsonProperty("maintenance_margin")
    private final BigDecimalValueWrapper maintenanceMargin;

    @JsonProperty("daily_pnl")
    private final BigDecimalValueWrapper dailyPnl;

    @JsonProperty("unrealized_pnl")
    private final BigDecimalValueWrapper unrealizedPnl;

}