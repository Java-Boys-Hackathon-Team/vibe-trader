package ru.javaboys.vibetraderbackend.chat.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptCleanupService {

    private final PromptRepository promptRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Clears the prompts table in a separate transaction and commits immediately.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clearPrompts() {
        log.info("Clearing prompts table before processing user message");
        // deleteAllInBatch issues a single delete statement (no entity loading)
        promptRepository.deleteAllInBatch();
        // Ensure changes are flushed within this REQUIRES_NEW transaction
        entityManager.flush();
        log.info("Prompts table cleared and committed");
    }
}
