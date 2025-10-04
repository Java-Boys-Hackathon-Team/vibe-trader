package ru.javaboys.vibetraderbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import ru.javaboys.vibetraderbackend.config.LlmProperties;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;

@EnableFeignClients(basePackages = "ru.javaboys.vibetraderbackend.finam.client.api")
@SpringBootApplication
@EnableConfigurationProperties({LlmProperties.class})
public class VibeTraderBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VibeTraderBackendApplication.class, args);
    }

}
