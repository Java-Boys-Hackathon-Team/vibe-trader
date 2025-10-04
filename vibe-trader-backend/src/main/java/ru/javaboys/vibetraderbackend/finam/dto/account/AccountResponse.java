package ru.javaboys.vibetraderbackend.finam.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

import java.util.List;

@Getter
@Builder
public class AccountResponse {

    @JsonProperty("account_id")
    private final String accountId;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("equity")
    private final BigDecimalValueWrapper equity;

    @JsonProperty("unrealized_profit")
    private final BigDecimalValueWrapper unrealizedProfit;

    @JsonProperty("positions")
    private final List<Position> positions;

    @JsonProperty("cash")
    private final List<Cash> cash;

    @JsonProperty("portfolio_mc")
    private final PortfolioMc portfolioMc;

}