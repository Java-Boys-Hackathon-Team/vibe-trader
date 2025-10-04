package ru.javaboys.vibetraderbackend.finam.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthResponse;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;

@RequiredArgsConstructor
public class FinamClientAuthInterceptor implements RequestInterceptor {
    private final SessionsApiV1 SessionsApi;
    private final String secret;

    private TokenInfo tokenInfo;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getTokenInfo().getToken();
        requestTemplate.header("Authorization", token);
    }

    private TokenInfo getTokenInfo() {
        if (tokenInfo == null || tokenInfo.getExpiresAt().isAfter(Instant.now().plus(3, ChronoUnit.MINUTES))) {
            AuthResponse token = SessionsApi.auth(AuthRequest.builder().secret(secret).build());
            TokenDetailsResponse tokenResponse = SessionsApi.tokenDetails(TokenDetailsRequest.builder().token(token.getToken()).build());
            tokenInfo = new TokenInfo(token.getToken(), tokenResponse.getExpiresAt());
        }

        return tokenInfo;
    }

}
