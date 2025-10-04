package ru.javaboys.vibetraderbackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    // New endpoint variant to support optional file upload along with other data
    @PostMapping(value = "/dialogs/{dialogId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SendMessageResponse sendMessageWithFile(@PathVariable Long dialogId,
                                                   @RequestPart("data") SendMessageRequest request,
                                                   @RequestPart(value = "file", required = false) MultipartFile file) {
        String content = request != null ? request.getContent() : null;
        boolean noContent = (content == null || content.trim().isEmpty());
        boolean noFile = (file == null || file.isEmpty());
        if (noContent && noFile) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Either content or file must be provided");
        }
        return chatService.sendUserMessage(dialogId, content == null ? "" : content, file);
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
