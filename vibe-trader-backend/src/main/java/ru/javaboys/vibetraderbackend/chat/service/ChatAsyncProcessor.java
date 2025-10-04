package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.model.*;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.DialogRepository;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;
import ru.javaboys.vibetraderbackend.llm.LlmRequest;
import ru.javaboys.vibetraderbackend.llm.LlmService;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAsyncProcessor {

    private final LlmService llmService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAsyncTaskRepository taskRepository;
    private final DialogRepository dialogRepository;

    @Async("taskExecutor")
    @Transactional
    public void processUserMessage(Long taskId, Long dialogId, String userContent) {
        log.info("Start processing task {} for dialog {}", taskId, dialogId);
        UserAsyncTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new IllegalArgumentException("Dialog not found: " + dialogId));
        try {
            // Simulate LLM processing time
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

            String answer = llmService.call(LlmRequest.builder()
                    .conversationId(String.valueOf(dialogId))
                    .userMessage(userContent)
                    .build());

            ChatMessage assistantMessage = ChatMessage.builder()
                    .dialog(dialog)
                    .task(task)
                    .role(MessageRole.ASSISTANT)
                    .content(answer)
                    .build();
            chatMessageRepository.save(assistantMessage);

            task.setStatus(TaskStatus.DONE);
            task.setCompletedAt(OffsetDateTime.now());
            taskRepository.save(task);
            log.info("Task {} completed", taskId);
        } catch (Exception e) {
            log.error("Task {} failed: {}", taskId, e.getMessage(), e);
            task.setStatus(TaskStatus.ERROR);
            task.setErrorMessage(e.getMessage());
            task.setCompletedAt(OffsetDateTime.now());
            taskRepository.save(task);
        }
    }
}
