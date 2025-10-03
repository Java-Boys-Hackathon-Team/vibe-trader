package ru.javaboys.vibetraderbackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javaboys.vibetraderbackend.chat.dto.*;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;
import ru.javaboys.vibetraderbackend.chat.model.Dialog;
import ru.javaboys.vibetraderbackend.chat.model.UserAsyncTask;
import ru.javaboys.vibetraderbackend.chat.service.ChatService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final ChatService chatService;

    @GetMapping("/dialogs")
    public List<DialogDto> getDialogs() {
        List<Dialog> dialogs = chatService.getDialogs();
        return dialogs.stream()
                .map(d -> DialogDto.builder()
                        .id(d.getId())
                        .title(d.getTitle())
                        .createdAt(d.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/dialogs")
    public DialogDto createDialog(@RequestBody @Validated CreateDialogRequest request) {
        Dialog dialog = chatService.createDialog(request.getTitle());
        return DialogDto.builder()
                .id(dialog.getId())
                .title(dialog.getTitle())
                .createdAt(dialog.getCreatedAt())
                .build();
    }

    @GetMapping("/dialogs/{dialogId}/messages")
    public List<ChatMessageDto> getDialogMessages(@PathVariable Long dialogId) {
        List<ChatMessage> messages = chatService.getDialogMessages(dialogId);
        return messages.stream().map(m -> ChatMessageDto.builder()
                        .id(m.getId())
                        .dialogId(m.getDialog().getId())
                        .taskId(m.getTask().getId())
                        .role(m.getRole())
                        .content(m.getContent())
                        .createdAt(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/dialogs/{dialogId}/messages")
    public SendMessageResponse sendMessage(@PathVariable Long dialogId, @RequestBody @Validated SendMessageRequest request) {
        return chatService.sendUserMessage(dialogId, request.getContent());
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        UserAsyncTask task = chatService.getTask(taskId);
        TaskDto dto = TaskDto.builder()
                .id(task.getId())
                .status(task.getStatus())
                .errorMessage(task.getErrorMessage())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
        return ResponseEntity.ok(dto);
    }
}
