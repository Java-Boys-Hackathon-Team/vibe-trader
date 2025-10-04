package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;
import ru.javaboys.vibetraderbackend.chat.model.HttpMethodType;
import ru.javaboys.vibetraderbackend.chat.model.Prompt;
import ru.javaboys.vibetraderbackend.chat.model.Submission;
import ru.javaboys.vibetraderbackend.chat.repository.SubmissionRepository;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private static final Logger log = LoggerFactory.getLogger(SubmissionService.class);

    private final SubmissionRepository submissionRepository;

    /**
     * Upsert submission by promt_uid. If a record exists, updates it; otherwise creates a new one.
     * Concurrency-safe with a retry on unique constraint violation.
     */
    @Transactional
    public Submission upsertByPromtUid(String promtUid,
                                       HttpMethodType type,
                                       String request,
                                       Prompt prompt,
                                       ChatMessage assistantMessage) {
        validateRequestPathAndQuery(request);
        int attempts = 0;
        while (true) {
            attempts++;
            Optional<Submission> existingOpt = submissionRepository.findByPromtUid(promtUid);
            try {
                Submission s = existingOpt.orElseGet(Submission::new);
                s.setPromtUid(promtUid);
                s.setType(type);
                s.setRequest(request);
                s.setPrompt(prompt);
                s.setAssistantMessage(assistantMessage);
                return submissionRepository.saveAndFlush(s);
            } catch (DataIntegrityViolationException ex) {
                if (attempts >= 2) {
                    log.warn("Upsert retry exhausted for promt_uid={} due to {}", promtUid, ex.getMessage());
                    throw ex;
                }
                // likely unique constraint race; re-read and retry
                existingOpt = submissionRepository.findByPromtUid(promtUid);
                if (existingOpt.isEmpty()) {
                    // small backoff path; continue loop to try again
                    continue;
                }
            }
        }
    }

    private void validateRequestPathAndQuery(String request) {
        if (request == null || request.isBlank()) {
            throw new IllegalArgumentException("request must not be blank");
        }
        if (!request.startsWith("/")) {
            throw new IllegalArgumentException("request must start with '/'");
        }
        try {
            URI uri = URI.create(request);
            if (uri.getScheme() != null || uri.getHost() != null) {
                throw new IllegalArgumentException("request must be a path and optional query only");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid request path/query format", e);
        }
    }
}
