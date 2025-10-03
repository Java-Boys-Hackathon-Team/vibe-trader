package ru.javaboys.vibetraderbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.javaboys.vibetraderbackend.config.LlmProperties;

@SpringBootApplication
@EnableConfigurationProperties({LlmProperties.class})
public class VibeTraderBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeTraderBackendApplication.class, args);
	}

}
