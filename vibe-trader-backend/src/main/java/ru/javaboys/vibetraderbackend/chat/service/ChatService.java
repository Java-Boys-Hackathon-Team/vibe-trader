package ru.javaboys.vibetraderbackend.chat.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.javaboys.vibetraderbackend.chat.dto.SendMessageResponse;
import ru.javaboys.vibetraderbackend.chat.model.*;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.DialogRepository;
import ru.javaboys.vibetraderbackend.chat.repository.PromptRepository;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final DialogRepository dialogRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAsyncTaskRepository taskRepository;
    private final PromptRepository promptRepository;
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
        asyncProcessor.processUserMessage(task.getId(), dialog.getId(), userMessage.getId(), content, false);

        return SendMessageResponse.builder()
                .taskId(task.getId())
                .userMessageId(userMessage.getId())
                .status(task.getStatus())
                .build();
    }

    @Transactional
    public SendMessageResponse sendUserMessage(Long dialogId, String content, MultipartFile file) {
        // 1) Resolve dialog
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new IllegalArgumentException("Dialog not found: " + dialogId));

        // 2) If CSV is provided, parse it first into memory to fail fast before creating task/message
        List<Prompt> parsedPrompts = new ArrayList<>();
        boolean isCsv = false;
        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            String ext = (filename != null) ? StringUtils.getFilenameExtension(filename) : null;
            isCsv = ext != null && ext.equalsIgnoreCase("csv");
            if (isCsv) {
                log.info("Accepted CSV file '{}' for dialog {}", filename, dialogId);
                try (InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
                    CSVFormat format = CSVFormat.DEFAULT.builder()
                            .setDelimiter(';')
                            .setIgnoreSurroundingSpaces(true)
                            .setTrim(true)
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build();
                    try (CSVParser parser = new CSVParser(reader, format)) {
                        List<CSVRecord> records = parser.getRecords();
                        if (records.isEmpty()) {
                            log.warn("CSV file '{}' contains no data rows", filename);
                        }
                        for (CSVRecord rec : records) {
                            String uid = null;
                            String question = null;
                            try {
                                uid = rec.get("uid");
                                question = rec.isMapped("question") ? rec.get("question") : null;
                            } catch (IllegalArgumentException ex) {
                                throw new IllegalArgumentException("Invalid CSV header. Expected 'uid;question' columns.", ex);
                            }
                            if (uid == null || uid.isBlank()) {
                                log.debug("Skipping row without uid: {}", rec);
                                continue;
                            }
                            if (question == null) {
                                question = "";
                            }
                            // Temporarily create Prompt without chatMessage; we'll attach after message is saved
                            parsedPrompts.add(Prompt.builder()
                                    .uid(uid.trim())
                                    .question(question)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid CSV file. Expected header 'uid;question' and UTF-8 encoding.", e);
                }
            } else {
                log.info("Accepted file '{}' (ext={}) for dialog {}", filename, ext, dialogId);
            }
        } else {
            log.info("No file provided for dialog {}", dialogId);
        }

        // 3) Create async task and user message
        UserAsyncTask task = taskRepository.save(UserAsyncTask.builder().status(TaskStatus.RUNNING).build());
        ChatMessage userMessage = ChatMessage.builder()
                .dialog(dialog)
                .task(task)
                .role(MessageRole.USER)
                .content(content)
                .build();
        userMessage = chatMessageRepository.save(userMessage);

        // 4) Persist parsed prompts (if any) linked to this ChatMessage
        if (isCsv && !parsedPrompts.isEmpty()) {
            var messageRef = chatMessageRepository.getReferenceById(userMessage.getId());
            for (Prompt p : parsedPrompts) {
                p.setChatMessage(messageRef);
            }
            promptRepository.saveAll(parsedPrompts);
            log.info("Saved {} prompt rows for chatMessage {}", parsedPrompts.size(), userMessage.getId());
        }

        // 5) Trigger async processing
        asyncProcessor.processUserMessage(task.getId(), dialog.getId(), userMessage.getId(), content, isCsv);

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
