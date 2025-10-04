package ru.javaboys.vibetraderbackend.chat.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    // content can be blank when a file is provided
    private String content;
}
