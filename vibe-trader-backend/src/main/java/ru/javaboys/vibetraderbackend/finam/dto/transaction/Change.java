package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Change {

    @JsonProperty("currency_code")
    private final String currencyCode;

    @JsonProperty("units")
    private final String units;

    @JsonProperty("nanos")
    private final Integer nanos;

}