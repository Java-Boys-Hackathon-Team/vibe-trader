package ru.javaboys.vibetraderbackend.finam.client;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {
    private final String token;
    private final Instant expiresAt;

}
