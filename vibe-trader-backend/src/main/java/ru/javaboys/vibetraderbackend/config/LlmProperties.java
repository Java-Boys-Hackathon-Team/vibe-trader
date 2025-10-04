package ru.javaboys.vibetraderbackend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "llm")
public class LlmProperties {
    private final Integer timeoutSeconds;

}
