package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.dto.SendMessageResponse;
import ru.javaboys.vibetraderbackend.chat.model.*;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.DialogRepository;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final DialogRepository dialogRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAsyncTaskRepository taskRepository;
    private final ChatAsyncProcessor asyncProcessor;

    @Transactional
    public Dialog createDialog(String title) {
        Dialog dialog = Dialog.builder().title(title).build();
        return dialogRepository.save(dialog);
    }

    @Transactional(readOnly = true)
    public List<Dialog> getDialogs() {
        return dialogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getDialogMessages(Long dialogId) {
        return chatMessageRepository.findByDialog_IdOrderByCreatedAtAsc(dialogId);
    }

    @Transactional
    public SendMessageResponse sendUserMessage(Long dialogId, String content) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new IllegalArgumentException("Dialog not found: " + dialogId));

        // Create async task in RUNNING state
        UserAsyncTask task = taskRepository.save(UserAsyncTask.builder().status(TaskStatus.RUNNING).build());

        // Save user message bound to the task
        ChatMessage userMessage = ChatMessage.builder()
                .dialog(dialog)
                .task(task)
                .role(MessageRole.USER)
                .content(content)
                .build();
        userMessage = chatMessageRepository.save(userMessage);

        // Trigger async processing for assistant response
        asyncProcessor.processUserMessage(task.getId(), dialog.getId(), content);

        return SendMessageResponse.builder()
                .taskId(task.getId())
                .userMessageId(userMessage.getId())
                .status(task.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public UserAsyncTask getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }
}
