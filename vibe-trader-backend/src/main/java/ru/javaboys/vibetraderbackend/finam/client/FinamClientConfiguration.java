package ru.javaboys.vibetraderbackend.finam.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.RequestInterceptor;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;

@Configuration
public class FinamClientConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor apiKeyRequestInterceptor(
            SessionsApiV1 sessionsApi,
            @Value("${finam-api.secret}") String secret
    ) {
        return new FinamClientAuthInterceptor(sessionsApi, secret);
    }

}
