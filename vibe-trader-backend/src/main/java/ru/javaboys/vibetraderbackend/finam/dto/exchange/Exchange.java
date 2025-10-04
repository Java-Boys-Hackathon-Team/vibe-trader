package ru.javaboys.vibetraderbackend.finam.dto.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Exchange {

    @JsonProperty("mic")
    private final String mic;

    @JsonProperty("name")
    private final String name;

}