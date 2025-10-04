package ru.javaboys.vibetraderbackend.finam.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import ru.javaboys.vibetraderbackend.finam.client.FinamClientConfiguration;
import ru.javaboys.vibetraderbackend.finam.dto.exchange.ExchangesResponse;

@FeignClient(
        name = "finam-exchanges-api",
        url = "${finam-api.url}/v1/exchanges",
        configuration = FinamClientConfiguration.class
)
public interface ExchangesApiV1 {

    @GetMapping(value = "")
    ExchangesResponse exchanges();

}
