package ru.javaboys.vibetraderbackend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
public class DialogDto {
    private Long id;
    private String title;
    private OffsetDateTime createdAt;
}
