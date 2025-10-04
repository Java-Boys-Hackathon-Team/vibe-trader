package ru.javaboys.vibetraderbackend.chat.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.javaboys.vibetraderbackend.agent.PromptTemplates;
import ru.javaboys.vibetraderbackend.agent.ctx.AssistantMessageContextHolder;
import ru.javaboys.vibetraderbackend.agent.tools.AccountsServiceTools;
import ru.javaboys.vibetraderbackend.agent.tools.AssetsServiceTools;
import ru.javaboys.vibetraderbackend.agent.tools.ExchangesServiceTools;
import ru.javaboys.vibetraderbackend.agent.tools.InstrumentsServiceTools;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;
import ru.javaboys.vibetraderbackend.chat.model.Dialog;
import ru.javaboys.vibetraderbackend.chat.model.MessageRole;
import ru.javaboys.vibetraderbackend.chat.model.Prompt;
import ru.javaboys.vibetraderbackend.chat.model.TaskStatus;
import ru.javaboys.vibetraderbackend.chat.model.UserAsyncTask;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.DialogRepository;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;
import ru.javaboys.vibetraderbackend.llm.LlmRequest;
import ru.javaboys.vibetraderbackend.llm.LlmService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAsyncProcessor {

    private final LlmService llmService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAsyncTaskRepository taskRepository;
    private final DialogRepository dialogRepository;
    private final PromptRepository promptRepository;

    private final AccountsServiceTools accountsTools;
    private final AssetsServiceTools assetsTools;
    private final InstrumentsServiceTools instrumentsTools;
    private final ExchangesServiceTools exchangesTools;

    @Async("taskExecutor")
    @Transactional
    public void processUserMessage(Long taskId, Long dialogId, Long userMessageId, String userContent, boolean hadCsv) {
        log.info("Start processing task {} for dialog {}", taskId, dialogId);
        UserAsyncTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new IllegalArgumentException("Dialog not found: " + dialogId));
        try {
            ChatMessage assistantMessage = ChatMessage.builder()
                    .dialog(dialog)
                    .task(task)
                    .role(MessageRole.ASSISTANT)
                    .content("Готовлю ответ и формирую submission.csv ...")
                    .build();
            var assistantMessageSaved = chatMessageRepository.save(assistantMessage);

            List<Object> tools = List.of(accountsTools, assetsTools, instrumentsTools, exchangesTools);

            if (hadCsv) {
                List<Prompt> prompts = promptRepository.findByChatMessage_Id(userMessageId);
                if (!prompts.isEmpty()) {
                    log.info("=====> Processing {} prompts sequentially", prompts.size());
                    for (int i = 0; i < prompts.size(); i++) {
                        log.info("Running {} of {} prompts", i, prompts.size());

                        Prompt p = prompts.get(i);
                        final String promptUid = p.getUid();
                        final String question = p.getQuestion() == null ? "" : p.getQuestion();
                        AssistantMessageContextHolder.set(assistantMessageSaved);
                        try {
                            String user = """
                                    promptUid: {uid}
                                    question: {q}
                                    """;
                            llmService.call(
                                    LlmRequest.builder()
                                            .conversationId(String.valueOf(dialogId))
                                            .systemMessage(PromptTemplates.SYSTEM_MAPPING)
                                            .userMessage(user)
                                            .userVariables(Map.of("uid", promptUid, "q", question))
                                            .tools(tools)
                                            .build()
                            );
                        } finally {
                            AssistantMessageContextHolder.clear();
                        }

                    }
                    log.info("All {} prompts processed sequentially", prompts.size());
                } else {
                    log.info("No prompts found for userMessage {}. Skipping mapping mode.", userMessageId);
                }
            } else {
                String answer = llmService.call(LlmRequest.builder()
                        .conversationId(String.valueOf(dialogId))
                        .userMessage(userContent == null ? "" : userContent)
                        .tools(tools)
                        .build());
                assistantMessage.setContent(answer);
                chatMessageRepository.save(assistantMessage);
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
