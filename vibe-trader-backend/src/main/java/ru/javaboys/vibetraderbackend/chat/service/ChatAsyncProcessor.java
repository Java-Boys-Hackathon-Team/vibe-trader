package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.model.*;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.DialogRepository;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;
import ru.javaboys.vibetraderbackend.llm.LlmRequest;
import ru.javaboys.vibetraderbackend.llm.LlmService;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAsyncProcessor {

    private final LlmService llmService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAsyncTaskRepository taskRepository;
    private final DialogRepository dialogRepository;
    private final PromptRepository promptRepository;
    private final SubmissionService submissionService;

    @Async("taskExecutor")
    @Transactional
    public void processUserMessage(Long taskId, Long dialogId, Long userMessageId, String userContent, boolean hadCsv) {
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
            assistantMessage = chatMessageRepository.save(assistantMessage);

            // If CSV was uploaded for the triggering user message, create submissions
            if (hadCsv) {
                List<Prompt> prompts = promptRepository.findByChatMessage_Id(userMessageId);
                if (!prompts.isEmpty()) {
                    for (Prompt p : prompts) {
                        String req = (p.getQuestion() != null && p.getQuestion().startsWith("/"))
                                ? p.getQuestion() : "/submissions/" + p.getUid();
                        submissionService.upsertByPromtUid(
                                p.getUid(),
                                HttpMethodType.GET,
                                req,
                                p,
                                assistantMessage
                        );
                    }
                    log.info("Created/updated {} submissions for assistantMessage {}", prompts.size(), assistantMessage.getId());
                } else {
                    log.info("No prompts found for userMessage {}. Skipping submissions.", userMessageId);
                }
            }

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
