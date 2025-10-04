package ru.javaboys.vibetraderbackend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.javaboys.vibetraderbackend.chat.model.TaskStatus;

@Data
@AllArgsConstructor
@Builder
public class SendMessageResponse {
    private Long taskId;
    private Long userMessageId;
    private TaskStatus status;
}
