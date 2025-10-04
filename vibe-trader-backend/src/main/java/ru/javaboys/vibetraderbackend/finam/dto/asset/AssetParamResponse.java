package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class AssetParamResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("account_id")
    private final String accountId;

    @JsonProperty("tradeable")
    private final Boolean tradeable;

    @JsonProperty("longable")
    private final Availability longable;

    @JsonProperty("shortable")
    private final Availability shortable;

    @JsonProperty("long_risk_rate")
    private final BigDecimalValueWrapper longRiskRate;

    @JsonProperty("long_collateral")
    private final MoneyValue longCollateral;

    @JsonProperty("short_risk_rate")
    private final BigDecimalValueWrapper shortRiskRate;

    @JsonProperty("long_initial_margin")
    private final MoneyValue longInitialMargin;

}