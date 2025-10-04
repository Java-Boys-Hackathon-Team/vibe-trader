package ru.javaboys.vibetraderbackend.finam.client;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;

@Service
public class TokenInfoHolder {
    private final AtomicReference<TokenDetailsResponse> tokenInfo = new AtomicReference<>();

    public void setTokenInfo(TokenDetailsResponse tokenInfo) {
        this.tokenInfo.set(tokenInfo);
    }

    public TokenDetailsResponse getTokenInfo() {
        return tokenInfo.get();
    }

}
