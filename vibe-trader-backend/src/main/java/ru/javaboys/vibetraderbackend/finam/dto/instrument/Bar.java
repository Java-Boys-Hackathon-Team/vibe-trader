package ru.javaboys.vibetraderbackend.finam.dto.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class Bar {

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("open")
    private final BigDecimalValueWrapper open;

    @JsonProperty("high")
    private final BigDecimalValueWrapper high;

    @JsonProperty("low")
    private final BigDecimalValueWrapper low;

    @JsonProperty("close")
    private final BigDecimalValueWrapper close;

    @JsonProperty("volume")
    private final BigDecimalValueWrapper volume;
}