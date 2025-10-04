package ru.javaboys.vibetraderbackend.finam.client;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.javaboys.vibetraderbackend.finam.client.api.AccountsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.AssetsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.ExchangesApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.InstrumentsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;

@Getter
@Service
@RequiredArgsConstructor
public class FinamClient {
    private final AccountsApiV1 accountsApi;
    private final AssetsApiV1 assetsApi;
    private final ExchangesApiV1 exchangesApi;
    private final InstrumentsApiV1 instrumentsApi;
    private final SessionsApiV1 sessionsApi;

}
