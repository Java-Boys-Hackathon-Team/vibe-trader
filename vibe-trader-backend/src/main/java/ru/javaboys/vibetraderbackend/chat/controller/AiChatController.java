package ru.javaboys.vibetraderbackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.javaboys.vibetraderbackend.chat.dto.*;
import ru.javaboys.vibetraderbackend.chat.model.*;
import ru.javaboys.vibetraderbackend.chat.repository.ChatMessageRepository;
import ru.javaboys.vibetraderbackend.chat.repository.SubmissionRepository;
import ru.javaboys.vibetraderbackend.chat.service.ChatService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final SubmissionRepository submissionRepository;

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

    @GetMapping("/tasks/{taskId}/submission")
    public ResponseEntity<byte[]> downloadTaskSubmission(@PathVariable Long taskId) {
        // Validate task status is DONE
        UserAsyncTask task = chatService.getTask(taskId);
        if (task.getStatus() != TaskStatus.DONE) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(("Task " + taskId + " is not DONE yet").getBytes(StandardCharsets.UTF_8));
        }
        // Find assistant message for this task
        Optional<ChatMessage> assistantMsgOpt = chatMessageRepository.findFirstByTask_IdAndRole(taskId, MessageRole.ASSISTANT);
        if (assistantMsgOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Assistant message for task " + taskId + " not found").getBytes(StandardCharsets.UTF_8));
        }
        ChatMessage assistantMsg = assistantMsgOpt.get();
        var submissions = submissionRepository.findAllByAssistantMessage_Id(assistantMsg.getId());
        if (submissions == null || submissions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Submissions for task " + taskId + " not found").getBytes(StandardCharsets.UTF_8));
        }
        // Build CSV: uid,type,request
        StringBuilder sb = new StringBuilder();
        // UTF-8 BOM for better Excel compatibility
        sb.append('\uFEFF');
        sb.append("uid,type,request\n");
        for (var s : submissions) {
            sb.append(csv(s.getPromtUid())).append(',')
              .append(csv(String.valueOf(s.getType()))).append(',')
              .append(csv(s.getRequest())).append('\n');
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        String filename = "submissions_task_" + taskId + ".csv";
        // Set Content-Disposition with a simple ASCII-safe filename to avoid RFC 5987 encoded fallback headers
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private static String csv(String v) {
        if (v == null) return "";
        boolean mustQuote = v.contains(",") || v.contains("\n") || v.contains("\r") || v.contains("\"");
        String escaped = v.replace("\"", "\"\"");
        return mustQuote ? ('"' + escaped + '"') : escaped;
    }
}
