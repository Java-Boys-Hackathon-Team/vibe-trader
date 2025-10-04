package ru.javaboys.vibetraderbackend.finam.client;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import ru.javaboys.vibetraderbackend.agent.ctx.AssistantMessageContextHolder;
import ru.javaboys.vibetraderbackend.agent.ctx.PromptUidContextHolder;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;
import ru.javaboys.vibetraderbackend.chat.model.HttpMethodType;
import ru.javaboys.vibetraderbackend.chat.model.Prompt;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;
import ru.javaboys.vibetraderbackend.chat.service.SubmissionService;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class FinamApiSubmissionInterceptor implements RequestInterceptor {

    private final SubmissionService submissionService;
    private final PromptRepository promptRepository;

    public FinamApiSubmissionInterceptor(SubmissionService submissionService,
                                         PromptRepository promptRepository) {
        this.submissionService = submissionService;
        this.promptRepository = promptRepository;
    }

    @Override
    public void apply(RequestTemplate template) {
        String promptUid = PromptUidContextHolder.get();
        ChatMessage assistant = AssistantMessageContextHolder.get();

        if (promptUid == null || assistant == null) {
            return;
        }

        String method = template.method() != null ? template.method() : "GET";
        HttpMethodType type = mapMethod(method);

        String path = normalizePath(template.path());
        String query = buildQuery(template.queries());
        String request = query == null || query.isBlank() ? path : (path + "?" + query);

        Prompt prompt = promptRepository.findByUid(promptUid).orElse(null);
        if (prompt == null) {
            return;
        }

        submissionService.upsertByPromtUid(promptUid, type, request, prompt, assistant);

        template.header("X-Prompt-Uid", promptUid);
    }

    private HttpMethodType mapMethod(String m) {
        try {
            return HttpMethodType.valueOf(m.toUpperCase());
        } catch (Exception ignore) {
            return HttpMethodType.GET;
        }
    }

    private String normalizePath(String p) {
        if (p == null || p.isBlank()) return "/";
        return p.startsWith("/") ? p : "/" + p;
    }

    private String buildQuery(Map<String, java.util.Collection<String>> queries) {
        if (queries == null || queries.isEmpty()) return "";
        var parts = new ArrayList<>(queries.entrySet());
        parts.sort(Map.Entry.comparingByKey());
        return parts.stream()
                .flatMap(e -> {
                    String key = e.getKey();
                    return e.getValue().stream().map(v -> key + "=" + v);
                })
                .collect(Collectors.joining("&"));
    }
}
