package ru.javaboys.vibetraderbackend.finam.client;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;
import ru.javaboys.vibetraderbackend.chat.service.SubmissionService;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;

@Configuration
public class FinamClientConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor apiKeyRequestInterceptor(
            TokenInfoHolder tokenInfoHolder,
            SessionsApiV1 sessionsApi,
            @Value("${finam-api.secret}") String secret
    ) {
        return new FinamClientAuthInterceptor(
                tokenInfoHolder,
                sessionsApi,
                secret
        );
    }

    @Bean
    public RequestInterceptor finamApiSubmissionInterceptor(
            SubmissionService submissionService,
            PromptRepository promptRepository
    ) {
        return new FinamApiSubmissionInterceptor(submissionService, promptRepository);
    }
}
