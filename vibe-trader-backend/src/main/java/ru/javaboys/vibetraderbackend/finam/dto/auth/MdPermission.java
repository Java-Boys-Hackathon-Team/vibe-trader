package ru.javaboys.vibetraderbackend.finam.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MdPermission {

    @JsonProperty("quote_level")
    private final QuoteLevelType quoteLevel;

    @JsonProperty("delay_minutes")
    private final Long delayMinutes;

    @JsonProperty("mic")
    private final String mic;

}
