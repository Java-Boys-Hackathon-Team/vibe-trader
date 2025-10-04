package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Transaction {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("category")
    private final String category;
    // todo тоже самое что и TransactionCategoryType ?

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("change")
    private final Change change;

    @JsonProperty("trade")
    private final TransactionTrade trade;

    @JsonProperty("transaction_category")
    private final TransactionCategoryType transactionCategory;

    @JsonProperty("transaction_name")
    private final String transactionName;

}