package ru.javaboys.vibetraderbackend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.javaboys.vibetraderbackend.chat.model.TaskStatus;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private TaskStatus status;
    private String errorMessage;
    private OffsetDateTime createdAt;
    private OffsetDateTime completedAt;
}
