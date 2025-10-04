package ru.javaboys.vibetraderbackend.finam.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BigDecimalValueWrapper {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("value")
    private final BigDecimal value;

    public static BigDecimalValueWrapper valueOf(long value) {
        return BigDecimalValueWrapper.builder()
                .value(BigDecimal.valueOf(value))
                .build();
    }

    public static BigDecimalValueWrapper valueOf(double value) {
        return BigDecimalValueWrapper.builder()
                .value(BigDecimal.valueOf(value))
                .build();
    }

}