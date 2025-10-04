package ru.javaboys.vibetraderbackend.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDialogRequest {
    @NotBlank
    private String title;
}
