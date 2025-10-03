package ru.javaboys.vibetraderbackend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.javaboys.vibetraderbackend.chat.model.MessageRole;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long id;
    private Long dialogId;
    private Long taskId;
    private MessageRole role;
    private String content;
    private OffsetDateTime createdAt;
}
