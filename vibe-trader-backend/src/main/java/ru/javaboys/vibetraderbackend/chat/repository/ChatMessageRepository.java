package ru.javaboys.vibetraderbackend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByDialog_IdOrderByCreatedAtAsc(Long dialogId);
}
