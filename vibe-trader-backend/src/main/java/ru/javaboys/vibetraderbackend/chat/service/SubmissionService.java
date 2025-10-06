package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionService {

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

                submissionRepository.findByPromtUid(promtUid);
            }
        }
    }
}
