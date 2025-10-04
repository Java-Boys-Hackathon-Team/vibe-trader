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

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

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

        // Build request path + query without host
        String request = buildPathAndQuery(template);

        Prompt prompt = promptRepository.findByUid(promptUid).orElse(null);
        if (prompt == null) {
            return;
        }

        submissionService.upsertByPromtUid(promptUid, type, request, prompt, assistant);
    }

    private String buildPathAndQuery(RequestTemplate template) {
        String url = template.feignTarget().url() + template.url();
        String pathAndQuery;
        if (url == null || url.isBlank()) {
            pathAndQuery = "/";
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            // absolute URL, strip scheme/host
            try {
                URI uri = URI.create(url);
                String path = uri.getRawPath();
                String query = uri.getRawQuery();
                if (path == null || path.isBlank()) {
                    path = "/";
                }
                pathAndQuery = query == null ? path : path + "?" + query;
            } catch (IllegalArgumentException e) {
                // fallback to best-effort: remove scheme and host manually
                int idx = url.indexOf("//");
                if (idx > -1) {
                    int slash = url.indexOf('/', idx + 2);
                    pathAndQuery = slash >= 0 ? url.substring(slash) : "/";
                } else {
                    pathAndQuery = url.startsWith("/") ? url : "/" + url;
                }
            }
        } else {
            pathAndQuery = url.startsWith("/") ? url : "/" + url;
        }

        // If there is no query in the URL, try to append from template.queries()
        if (!pathAndQuery.contains("?")) {
            Map<String, Collection<String>> queries = template.queries();
            if (queries != null && !queries.isEmpty()) {
                StringJoiner sj = new StringJoiner("&");
                for (Map.Entry<String, Collection<String>> e : queries.entrySet()) {
                    String key = e.getKey();
                    Collection<String> values = e.getValue();
                    if (values == null || values.isEmpty()) {
                        sj.add(key);
                    } else {
                        for (String v : values) {
                            // values are assumed already encoded by Feign or provided as-is
                            sj.add(key + "=" + (v == null ? "" : v));
                        }
                    }
                }
                String q = sj.toString();
                if (!q.isEmpty()) {
                    pathAndQuery = pathAndQuery + "?" + q;
                }
            }
        }
        // Ensure it starts with '/'
        if (!pathAndQuery.startsWith("/")) {
            pathAndQuery = "/" + pathAndQuery;
        }
        return pathAndQuery;
    }

    private HttpMethodType mapMethod(String m) {
        try {
            return HttpMethodType.valueOf(m.toUpperCase());
        } catch (Exception ignore) {
            return HttpMethodType.GET;
        }
    }
}
