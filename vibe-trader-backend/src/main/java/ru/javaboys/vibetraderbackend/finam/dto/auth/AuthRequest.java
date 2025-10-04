package ru.javaboys.vibetraderbackend.finam.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {

    @JsonProperty("secret")
    private final String secret;

}
