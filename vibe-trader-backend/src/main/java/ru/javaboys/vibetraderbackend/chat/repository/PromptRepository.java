package ru.javaboys.vibetraderbackend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaboys.vibetraderbackend.chat.model.Prompt;

import java.util.List;
import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByChatMessage_Id(Long chatMessageId);

    Optional<Prompt> findByUid(String uid);
}
