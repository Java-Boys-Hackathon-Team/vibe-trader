package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionsResponse {

    @JsonProperty("transactions")
    private final List<Transaction> transactions;

}