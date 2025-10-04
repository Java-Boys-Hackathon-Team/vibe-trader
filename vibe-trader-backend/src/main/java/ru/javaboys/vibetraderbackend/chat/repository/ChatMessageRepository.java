package ru.javaboys.vibetraderbackend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;
import ru.javaboys.vibetraderbackend.chat.model.MessageRole;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByDialog_IdOrderByCreatedAtAsc(Long dialogId);
    Optional<ChatMessage> findFirstByTask_IdAndRole(Long taskId, MessageRole role);
}
