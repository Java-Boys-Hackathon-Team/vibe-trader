package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransactionsResponse {

    @JsonProperty("transactions")
    private final List<Transaction> transactions;

}