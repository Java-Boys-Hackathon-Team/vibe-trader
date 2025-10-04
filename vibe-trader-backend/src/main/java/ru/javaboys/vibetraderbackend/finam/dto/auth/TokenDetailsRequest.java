package ru.javaboys.vibetraderbackend.finam.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDetailsRequest {

    @JsonProperty("token")
    private final String token;

}
