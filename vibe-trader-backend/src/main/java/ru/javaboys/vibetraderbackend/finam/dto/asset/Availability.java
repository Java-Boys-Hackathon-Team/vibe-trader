package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Availability {

    @JsonProperty("value")
    private final String value;

    @JsonProperty("halted_days")
    private final Integer haltedDays;

}