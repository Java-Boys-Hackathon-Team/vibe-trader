package ru.javaboys.vibetraderbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncConfig {

    private final PromptsProcessingProperties props;

    public AsyncConfig(PromptsProcessingProperties props) {
        this.props = props;
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        int p = Math.max(1, props.getParallelism());
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(p);
        ex.setMaxPoolSize(Math.max(p, p * 2));
        ex.setQueueCapacity(100);
        ex.setThreadNamePrefix("task-");
        ex.initialize();
        return ex;
    }
}
