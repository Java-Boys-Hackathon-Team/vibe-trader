package ru.javaboys.vibetraderbackend.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class MockLlmService implements LlmService {
    @Override
    public String call(LlmRequest request) {
        String user = request.getUserMessage() != null ? request.getUserMessage() : "";
        String response = "[MOCK LLM] Ответ на: " + user;
        log.info("Mock LLM called. Input='{}', Output='{}'", user, response);
        return response;
    }

    @Override
    public <T> T callAs(LlmRequest request, Class<T> classType) {
        String result = call(request);
        if (classType.isAssignableFrom(String.class)) {
            return classType.cast(result);
        }
        throw new UnsupportedOperationException("MockLlmService supports only String responses for now");
    }
}
