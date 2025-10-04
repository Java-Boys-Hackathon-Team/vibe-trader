package ru.javaboys.vibetraderbackend.finam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DateValue {

    @JsonProperty("year")
    private final Integer year;

    @JsonProperty("month")
    private final Integer month;

    @JsonProperty("day")
    private final Integer day;

}