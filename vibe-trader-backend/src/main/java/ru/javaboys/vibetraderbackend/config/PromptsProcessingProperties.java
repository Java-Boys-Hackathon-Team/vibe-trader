package ru.javaboys.vibetraderbackend.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "prompts.processing")
public class PromptsProcessingProperties {
    private int parallelism = 1;

    public void setParallelism(int parallelism) {
        this.parallelism = Math.max(1, parallelism);
    }
}
