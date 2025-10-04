package ru.javaboys.vibetraderbackend.finam.client;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class TokenInfo {
    private final String token;
    private final Instant expiresAt;

}
