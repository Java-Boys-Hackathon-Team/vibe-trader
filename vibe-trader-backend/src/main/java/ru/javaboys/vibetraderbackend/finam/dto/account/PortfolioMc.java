package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class PortfolioMc {

    @JsonProperty("available_cash")
    private final BigDecimalValueWrapper availableCash;

    @JsonProperty("initial_margin")
    private final BigDecimalValueWrapper initialMargin;

    @JsonProperty("maintenance_margin")
    private final BigDecimalValueWrapper maintenanceMargin;

}